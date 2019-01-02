package org.wit.archaeologicalfieldwork.views.hillfort

import android.annotation.SuppressLint
import android.content.Intent
import android.widget.DatePicker
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.activity_hillfort.*
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import org.jetbrains.anko.*
import org.wit.archaeologicalfieldwork.R
import org.wit.archaeologicalfieldwork.helpers.*
import org.wit.archaeologicalfieldwork.models.*
import org.wit.archaeologicalfieldwork.models.stores.firebase.ImageFirebaseStore
import org.wit.archaeologicalfieldwork.views.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.concurrent.timerTask

class HillfortPresenter(view: BaseView) : BasePresenter(view), AnkoLogger{

    val locationRequest = createDefaultLocationRequest()
    var defaultLocation = Location(52.245696, -7.139102, 15f)
    var locationService: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(view)
    private lateinit var editedImage: Image
    private var editing: Boolean = false
    var date: Date? = null
    var hillfort = Hillfort()
    var map: GoogleMap? = null

    init{

        //show data changes if using firebase
        if(app.images is ImageFirebaseStore) {
            Timer().schedule(timerTask {
                async(UI) {
                    (app.images as ImageFirebaseStore).fetchImages { }
                    (view as HillfortView).showImages()
                }
            }, 0, imageRefreshTime)
        }

        if (view.intent.hasExtra("hillfort_edit")) {
            editing = true
            hillfort = view.intent.extras.getParcelable("hillfort_edit")
            view.showHillfort(hillfort)
        } else{
            async(UI){
                hillfort.id = app.hillforts.create(hillfort)
            }
            if (checkLocationPermissions(view)) {
                doSetCurrentLocation()
            } else{
              locationUpdate(defaultLocation)
            }
        }
    }

    fun doAddOrSave(){
        hillfort.name = view?.hillfortName!!.text.toString()
        hillfort.description = view?.hillfortDescription!!.text.toString()
        hillfort.addedBy = app.user.email!!

        if(view?.ratingBar!!.rating != 0f){
            async(UI) {
                var rating = Rating(hillfortId = hillfort.id, addedBy = app.user.email!!, rating = view?.ratingBar!!.rating)
                if(app.ratings.findAll().filter { it.hillfortId == hillfort.id && it.addedBy == app.user.email!! }.isEmpty()){
                        app.ratings.create(rating)
                }
                else{
                        app.ratings.update(rating)
                }
            }
        }
        else{
            async(UI){
                var ratingToDelete: Rating? = app.ratings.findAll().filter { it.hillfortId == hillfort.id }.find { it.addedBy == app.user.email!! }
                if(ratingToDelete != null){
                    app.ratings.delete(ratingToDelete)
                }
            }
        }

        if(view?.visitedCheckBox!!.isChecked){
            val visit = Visit()
            visit.hillfortId = hillfort.id
            visit.addedBy = app.user.email!!
            if(date != null){
                visit.date = date!!.time
            }
            else{
                visit.date = Date().time
            }
            async(UI) {
                app.visits.create(visit)
            }
        }
        else{
            async(UI) {
                app.visits.delete(app.visits.findAll().filter { it.hillfortId == hillfort.id }.find { it.addedBy == app.user.email!!}!!)
            }
        }
        if (hillfort.name.isNotEmpty()) {
            async(UI) {
                if (editing){app.hillforts.update(hillfort)}
                else {
                    app.hillforts.update(hillfort)
                }
                view?.finish()
                }
        } else{ view?.toast(R.string.no_title_toast) }
    }

    fun doCancel(){
        if(!editing){
            doDelete()
        }
        else{
            view?.finish()
        }
    }

    fun doDelete(){
        async(UI) {
                app.images.findAll().filter { it.hillfortId == hillfort.id }.forEach {
                    app.images.delete(it)
                }
                app.visits.findAll().filter { it.hillfortId == hillfort.id }.forEach {
                    app.visits.delete(it)
                }
                app.notes.findAll().filter { it.hillfortId == hillfort.id }.forEach {
                    app.notes.delete(it)
                }
                app.favourites.findAll().filter { it.hillfortId == hillfort.id }.forEach {
                    app.favourites.delete(it)
                }
                app.ratings.findAll().filter { it.hillfortId == hillfort.id }.forEach {
                    app.ratings.delete(it)
                }

            app.hillforts.delete(hillfort)
            view?.finish()
        }
    }

    fun doSelectImage(){
        view?.let{
            showImagePicker(view!!, ADD_IMAGE_REQUEST)
        }
    }

    fun doSetLocation() {
        view?.navigateTo(VIEW.LOCATION, LOCATION_REQUEST, "location", hillfort.location)
    }

    override fun doActivityResult(requestCode: Int, data: Intent?){
        async(UI) {
            (view as HillfortView).showImages()
        }
        when(requestCode){
            ADD_IMAGE_REQUEST -> {
                if(data != null){
                    async(UI) {
                        val image = Image(hillfortId = hillfort.id, data = data.data.toString(), addedBy = app.user.email!!)
                        image.id = app.images.create(image)
                    }
                }
            }
            IMAGE_EDIT_REQUEST -> {
                if(data != null){
                    editedImage.data = data.data.toString()
                    async(UI) {
                        app.images.update(editedImage)
                    }
                }
                else{
                    async(UI) {
                        app.images.delete(editedImage)
                    }
                }
            }
            LOCATION_REQUEST -> {
                if (data != null) {
                    locationUpdate(hillfort.location)
                    hillfort.location = data.extras.getParcelable("location")
                }
            }
        }
    }

    override fun doEditImage(image: Image) {
        editedImage = image
        showImagePicker(view!!, IMAGE_EDIT_REQUEST)
    }

    fun doSetVisited(){
        if(view?.visitedCheckBox!!.isChecked){
            view?.alert {
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
                    view?.visitedCheckBox!!.text = view?.resources!!.getString(R.string.visited_time, simplifyDate(date!!))
                }
                noButton {
                    view?.visitedCheckBox!!.text = null
                }
            }?.show()
        }
        else{
            view?.visitedCheckBox!!.setText(R.string.visited)
        }
    }

    fun locationUpdate(location: Location){
        hillfort.location = location
        view?.hillfortLat?.text = view?.resources!!.getString(R.string.hillfortLat, location.lat)
        view?.hillfortLng?.text = view?.resources!!.getString(R.string.hillfortLng, location.lng)
        map?.clear()
        map?.uiSettings?.isZoomControlsEnabled = true
        val options = MarkerOptions().title(hillfort.name).position(LatLng(location.lat, location.lng))
        map?.addMarker(options)
        map?.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(location.lat, location.lng), location.zoom))
        view?.showHillfort(hillfort)
    }

    fun doConfigureMap(map: GoogleMap) {
        this.map = map
        locationUpdate(hillfort.location)
    }

    override fun doRequestPermissionsResult(requestCode: Int, permission: Array<String>, grantResults: IntArray) {
        if (isPermissionGranted(requestCode, grantResults)) {
            doSetCurrentLocation()
        } else {
            // permissions denied, so use the default location
            locationUpdate(defaultLocation)
        }
    }

    @SuppressLint("MissingPermission")
    fun doResartLocationUpdates() {
        var locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                if (locationResult != null) {
                    val l = locationResult.locations.last()
                    locationUpdate(Location(l.latitude, l.longitude, hillfort.location.zoom))
                }
            }
        }
        if (!editing) {
            locationService.requestLocationUpdates(locationRequest, locationCallback, null)
        }
    }

    @SuppressLint("MissingPermission")
    fun doSetCurrentLocation() {
        locationService.lastLocation.addOnSuccessListener {
            locationUpdate(Location(it.latitude, it.longitude, 15f))
        }
    }

    fun doShare() {
        var shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "text/plain"
        var shareBody = "Check out this hillfort!"
        var shareSubject = "${hillfort.name}\nhttp://maps.google.com/maps?z=12&t=m&q=${hillfort.location.lat},${hillfort.location.lng}\n\n"
        async(UI){
            app.images.findAll().filter { it.hillfortId == hillfort.id }.forEach {
                shareSubject += it.data  + "\n\n"
            }
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, shareBody)
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareSubject)
            view?.startActivity(Intent.createChooser(shareIntent, "Share using"))
        }
    }
}