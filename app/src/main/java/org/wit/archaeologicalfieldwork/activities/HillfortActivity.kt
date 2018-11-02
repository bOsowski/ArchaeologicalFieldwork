package org.wit.archaeologicalfieldwork.activities

import android.app.DatePickerDialog
import android.app.FragmentManager
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.DatePicker
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_hillfort.*
import org.jetbrains.anko.*
import org.w3c.dom.Text
import org.wit.archaeologicalfieldwork.R
import org.wit.archaeologicalfieldwork.helpers.readImage
import org.wit.archaeologicalfieldwork.helpers.readImageFromPath
import org.wit.archaeologicalfieldwork.helpers.showImagePicker
import org.wit.archaeologicalfieldwork.main.MainApp
import org.wit.archaeologicalfieldwork.models.Hillfort
import org.wit.archaeologicalfieldwork.models.Location
import org.wit.archaeologicalfieldwork.models.Visit
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*
import javax.xml.datatype.DatatypeConstants.MONTHS

class HillfortActivity : AppCompatActivity(), AnkoLogger {

    val IMAGE_REQUEST = 1
    val LOCATION_REQUEST = 2
    val TIME_REQUEST = 3
    var hillfort = Hillfort()
    lateinit var app : MainApp

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hillfort)
        app = application as MainApp

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
            try{
                hillfortImage.setImageBitmap(readImageFromPath(this, hillfort.images.first()))
            }catch (e: Exception){

            }
            btnAdd.setText(R.string.button_editHillfort)
            chooseImage.setText(R.string.button_editImage)
            editing = true
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
            showImagePicker(this, IMAGE_REQUEST)
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
        when(requestCode){
            IMAGE_REQUEST -> {
                if(data != null){
                    hillfort.images.add(data.data.toString())
                    hillfortImage.setImageBitmap(readImage(this, resultCode, data))
                }
            }
            LOCATION_REQUEST -> {
                if (data != null) {
                    hillfort.location = data.extras.getParcelable("location")
                }
            }
        }
    }

    private fun simplifyDate(date: Date) : String{
        return SimpleDateFormat.getDateInstance().format(date).toString()
    }
}
