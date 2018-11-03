package org.wit.archaeologicalfieldwork.activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.os.Bundle
import android.widget.DatePicker
import kotlinx.android.synthetic.main.activity_hillfort.*
import org.jetbrains.anko.*
import org.wit.archaeologicalfieldwork.R
import org.wit.archaeologicalfieldwork.adapters.ImageAdapter
import org.wit.archaeologicalfieldwork.adapters.ImageListener
import org.wit.archaeologicalfieldwork.helpers.showImagePicker
import org.wit.archaeologicalfieldwork.main.MainApp
import org.wit.archaeologicalfieldwork.models.Hillfort
import org.wit.archaeologicalfieldwork.models.Location
import org.wit.archaeologicalfieldwork.models.Visit
import java.text.SimpleDateFormat
import java.util.*

class HillfortActivity : AppCompatActivity(), ImageListener, AnkoLogger {

    override fun onImageClick(image: String) {
        editedImage = image
        showImagePicker(this, IMAGE_EDIT_REQUEST)
    }

    val ADD_IMAGE_REQUEST = 1
    val IMAGE_EDIT_REQUEST = 2
    val LOCATION_REQUEST = 3

    lateinit var editedImage: String

    var hillfort = Hillfort()
    lateinit var app : MainApp

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hillfort)
        app = application as MainApp

        val layoutManager = LinearLayoutManager(this)
        recyclerViewImages.layoutManager = layoutManager

        cancel.setOnClickListener{
            info("Cancel pressed")
            finish()
        }

        var editing = false

        if (intent.hasExtra("hillfort_edit")) {
            hillfort = intent.extras.getParcelable("hillfort_edit")
            hillfortName.setText(hillfort.name)
            hillfortDescription.setText(hillfort.description)
            val visit = hillfort.visits.find { it.userId == app.currentUser.id }
            if(visit != null){
                visitedCheckBox.text = resources.getString(R.string.visited_time, simplifyDate(visit.date))
            }
            visitedCheckBox.isChecked = visit != null
            btnAdd.setText(R.string.button_editHillfort)
            //chooseImage.setText(R.string.button_editImage)
            editing = true
            showImages(hillfort.images)
        }

        var date: Date? = null

        visitedCheckBox.setOnClickListener {
            if(visitedCheckBox.isChecked){
                alert {
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
                        visitedCheckBox.text = resources.getString(R.string.visited_time, simplifyDate(date!!))
                    }
                    noButton {
                        visitedCheckBox.text = resources.getString(R.string.visited_time, simplifyDate(Date()))
                    }
                }.show()
            }
            else{
                visitedCheckBox.setText(R.string.visited)
            }
        }

        btnAdd.setOnClickListener {
            hillfort.name = hillfortName.text.toString()
            hillfort.description = hillfortDescription.text.toString()
            hillfort.addedBy = app.currentUser.id
            if(visitedCheckBox.isChecked){
                var visit = Visit()
                visit.userId = app.currentUser.id
                if(date != null){
                    visit.date = date!!
                }
                else{
                    visit.date = Date()
                }
                hillfort.visits.add(visit)
            }
            else{
                hillfort.visits.remove(hillfort.visits.find { it.userId == app.currentUser.id })
            }
            if (hillfort.name.isNotEmpty()) {
                if(editing) app.forts.update(hillfort.copy()) else app.forts.create(hillfort.copy())
                setResult(AppCompatActivity.RESULT_OK)
                finish()
            }else{
                toast(R.string.no_title_toast)
            }
        }

        chooseImage.setOnClickListener {
            showImagePicker(this, ADD_IMAGE_REQUEST)
        }

        hillfortLocation.setOnClickListener {
            var location = Location(52.245696, -7.139102, 15f)
            if(hillfort.location.zoom != 0f){
                location = hillfort.location
            }
            startActivityForResult(intentFor<MapsActivity>().putExtra("location", location), LOCATION_REQUEST)
        }

        delete.setOnClickListener {
            app.forts.delete(hillfort)
            finish()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        showImages(hillfort.images)
        when(requestCode){
            ADD_IMAGE_REQUEST -> {
                if(data != null){
                    hillfort.images.add(data.data.toString())
                }
            }
            IMAGE_EDIT_REQUEST -> {
                if(data != null){
                    hillfort.images[hillfort.images.indexOf(editedImage)] = data.data.toString()
                }
                else{
                    hillfort.images.remove(editedImage)
                }
            }
            LOCATION_REQUEST -> {
                if (data != null) {
                    hillfort.location = data.extras.getParcelable("location")
                }
            }
        }
    }

    fun showImages(images: List<String>){
        recyclerViewImages.adapter = ImageAdapter(images, this)
        recyclerViewImages.adapter?.notifyDataSetChanged()
    }

    private fun simplifyDate(date: Date) : String{
        return SimpleDateFormat.getDateInstance().format(date).toString()
    }
}
