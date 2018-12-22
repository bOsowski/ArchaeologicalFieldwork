package org.wit.archaeologicalfieldwork.views.hillfort

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.widget.DatePicker
import kotlinx.android.synthetic.main.activity_hillfort.*
import org.jetbrains.anko.*
import org.wit.archaeologicalfieldwork.R
import org.wit.archaeologicalfieldwork.adapters.ImageAdapter
import org.wit.archaeologicalfieldwork.helpers.showImagePicker
import org.wit.archaeologicalfieldwork.main.MainApp
import org.wit.archaeologicalfieldwork.models.Hillfort
import org.wit.archaeologicalfieldwork.models.Location
import org.wit.archaeologicalfieldwork.models.Visit
import org.wit.archaeologicalfieldwork.views.editLocation.EditLocationView
import java.text.SimpleDateFormat
import java.util.*

class HillfortPresenter(private val view: HillfortView){
    private val ADD_IMAGE_REQUEST = 1
    private val IMAGE_EDIT_REQUEST = 2
    private val LOCATION_REQUEST = 3

    private lateinit var editedImage: String
    var app : MainApp = view.application as MainApp
    var date: Date? = null

    private var editing: Boolean = false

    var hillfort = Hillfort()

    init{
        if (view.intent.hasExtra("hillfort_edit")) {
//            app.currentFort = view.intent.extras.getParcelable("hillfort_edit")
            view.hillfortName.setText(app.currentFort.name)
            view.hillfortDescription.setText(app.currentFort.description)
            val visit = app.currentFort.visits.find { it.userId == app.currentUser.id }
            if(visit != null){
                view.visitedCheckBox.text = view.resources.getString(R.string.visited_time, simplifyDate(visit.date))
            }
            view.visitedCheckBox.isChecked = visit != null
            view.btnAdd.setText(R.string.button_editHillfort)
            editing = true
            showImages(app.currentFort.images)
        }
    }

    fun doAddOrSave(){
        app.currentFort.name = view.hillfortName.text.toString()
        app.currentFort.description = view.hillfortDescription.text.toString()
        app.currentFort.addedBy = app.currentUser.id

        if(view.visitedCheckBox.isChecked){
            val visit = Visit()
            visit.userId = app.currentUser.id
            if(date != null){
                visit.date = date!!
            }
            else{
                visit.date = Date()
            }
            app.currentFort.visits.add(visit)
        }
        else{
            app.currentFort.visits.remove(app.currentFort.visits.find { it.userId == app.currentUser.id })
        }
        if (app.currentFort.name.isNotEmpty()) {
            if(editing) app.data.update(app.currentFort.copy()) else app.data.create(app.currentFort.copy())
            view.setResult(AppCompatActivity.RESULT_OK)
            view.finish()
        }else{
            view.toast(R.string.no_title_toast)
        }
    }

    fun doCancel(){
        view.finish()
    }

    fun doDelete(){
        app.data.delete(hillfort)
        app.currentFort = Hillfort()
        view.finish()
    }

    fun doSelectImage(){
        showImagePicker(view, ADD_IMAGE_REQUEST)
    }

    fun doSetLocation(){
        var location = Location(52.245696, -7.139102, 15f)
        if(app.currentFort.location.zoom != 0f){
            location = app.currentFort.location
        }
        view.startActivityForResult(view.intentFor<EditLocationView>().putExtra("location", location), LOCATION_REQUEST)
    }

    fun doActivityResult(requestCode: Int, data: Intent?){
        showImages(app.currentFort.images)
        when(requestCode){
            ADD_IMAGE_REQUEST -> {
                if(data != null){
                    app.currentFort.images.add(data.data.toString())
                }
            }
            IMAGE_EDIT_REQUEST -> {
                if(data != null){
                    app.currentFort.images[app.currentFort.images.indexOf(editedImage)] = data.data.toString()
                }
                else{
                    app.currentFort.images.remove(editedImage)
                }
            }
            LOCATION_REQUEST -> {
                if (data != null) {
                    app.currentFort.location = data.extras.getParcelable("location")
                }
            }
        }
    }

    fun doEditImage(image: String) {
        editedImage = image
        showImagePicker(view, IMAGE_EDIT_REQUEST)
    }

    fun doSetVisited(){
        if(view.visitedCheckBox.isChecked){
            view.alert {
                isCancelable = false
                lateinit var datePicker: DatePicker
                customView {
                    verticalLayout {
                        datePicker = datePicker {
                            maxDate = System.currentTimeMillis()
                        }
                    }
                }
                yesButton {
                    val rawDate = "${datePicker.dayOfMonth}/${datePicker.month + 1}/${datePicker.year}"
                    val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)
                    date = sdf.parse(rawDate)
                    view.visitedCheckBox.text = view.resources.getString(R.string.visited_time, simplifyDate(date!!))
                }
                noButton {
                    view.visitedCheckBox.text = null //resources.getString(R.string.visited_time, simplifyDate(Date()))
                }
            }.show()
        }
        else{
            view.visitedCheckBox.setText(R.string.visited)
        }
    }

    private fun simplifyDate(date: Date) : String{
        return SimpleDateFormat.getDateInstance().format(date).toString()
    }

    private fun showImages(images: List<String>){
        view.recyclerViewImages.adapter = ImageAdapter(images, view)
        view.recyclerViewImages.adapter?.notifyDataSetChanged()
    }
}