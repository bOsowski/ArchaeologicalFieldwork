package org.wit.archaeologicalfieldwork.activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_hillfort.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.toast
import org.wit.archaeologicalfieldwork.R
import org.wit.archaeologicalfieldwork.helpers.readImage
import org.wit.archaeologicalfieldwork.helpers.readImageFromPath
import org.wit.archaeologicalfieldwork.helpers.showImagePicker
import org.wit.archaeologicalfieldwork.main.MainApp
import org.wit.archaeologicalfieldwork.models.Hillfort
import org.wit.archaeologicalfieldwork.models.Location
import java.lang.Exception

class HillfortActivity : AppCompatActivity(), AnkoLogger {

    val IMAGE_REQUEST = 1
    val LOCATION_REQUEST = 2
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
            hillfort = intent.extras.getParcelable<Hillfort>("hillfort_edit")
            hillfortName.setText(hillfort.name)
            hillfortDescription.setText(hillfort.description)
            try{
                hillfortImage.setImageBitmap(readImageFromPath(this, hillfort.images.first()))
            }catch (e: Exception){

            }
            btnAdd.setText(R.string.button_editHillfort)
            chooseImage.setText(R.string.button_editImage)
            editing = true
        }

        btnAdd.setOnClickListener {
            hillfort.name = hillfortName.text.toString()
            hillfort.description = hillfortDescription.text.toString()
            hillfort.addedBy = app.currentUser.id
            if (hillfort.name.isNotEmpty()) {
                if(editing) app.forts.update(hillfort.copy()) else app.forts.create(hillfort.copy())
                info("'${btnAdd.text}' Button Pressed: $hillfortName")
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
}
