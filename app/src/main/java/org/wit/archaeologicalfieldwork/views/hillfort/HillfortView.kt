package org.wit.archaeologicalfieldwork.views.hillfort

import androidx.recyclerview.widget.LinearLayoutManager
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ShareActionProvider
import androidx.core.view.MenuItemCompat
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import kotlinx.coroutines.experimental.android.UI
import com.google.android.gms.maps.GoogleMap
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_hillfort.*
import kotlinx.coroutines.experimental.async
import org.jetbrains.anko.*
import org.wit.archaeologicalfieldwork.R
import org.wit.archaeologicalfieldwork.activities.NotesActivity
import org.wit.archaeologicalfieldwork.adapters.ImageAdapter
import org.wit.archaeologicalfieldwork.helpers.simplifyDate
import org.wit.archaeologicalfieldwork.main.MainApp
import org.wit.archaeologicalfieldwork.models.Hillfort
import org.wit.archaeologicalfieldwork.views.BaseView
import java.util.*

class HillfortView : BaseView() {

    private lateinit var presenter: HillfortPresenter
    lateinit var map: GoogleMap
    var mShareActionProvider: ShareActionProvider? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hillfort)
        mapView2.onCreate(savedInstanceState)
        mapView2.getMapAsync {
            map = it
            map.setOnMapClickListener { _ ->
                presenter.doSetLocation()
            }
            presenter.doConfigureMap(map)
        }

        toolbarAdd.title = title
        setSupportActionBar(toolbarAdd)

        presenter = initPresenter(HillfortPresenter(this)) as HillfortPresenter

        visitedCheckBox.setOnClickListener {
            presenter.doSetVisited()
        }

        chooseImage.setOnClickListener {
            presenter.doSelectImage()
        }

        takePicture.setOnClickListener {
            presenter.doTakePicture()
        }

        refresh.setOnClickListener {
            presenter.doShowImages()
        }

        viewNotes.setOnClickListener {
            info("View notes clicked.")
            startActivity(intentFor<NotesActivity>().putExtra("hillfort_data", presenter.hillfort))
            finish()
        }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_hillfort, menu)

        menu.findItem(R.id.action_share).also { menuItem ->
            // Fetch and store ShareActionProvider
            mShareActionProvider = MenuItemCompat.getActionProvider(menuItem) as? ShareActionProvider
        }

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_save -> presenter.doAddOrSave()
            R.id.item_cancel -> presenter.doCancel()
            R.id.item_delete -> presenter.doDelete()
            R.id.action_share -> presenter.doShare()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun showHillfort(hillfort: Hillfort){
        val layoutManager = LinearLayoutManager(this)
        recyclerViewImages.layoutManager = layoutManager
        hillfortName.setText(hillfort.name)
        hillfortDescription.setText(hillfort.description)

        async(UI) {
            val visit = presenter.app.visits.findAll().filter{it.hillfortId == hillfort.id}.find { it.addedBy == FirebaseAuth.getInstance().currentUser?.email }
            if(visit != null){
                visitedCheckBox.text = resources.getString(R.string.visited_time, simplifyDate(Date(visit.date)))
            }
            visitedCheckBox.isChecked = visit != null
            showImages()
        }

        async(UI){
            val currentUserRating = presenter.app.ratings.findAll().find { it.hillfortId == hillfort.id && it.addedBy == FirebaseAuth.getInstance().currentUser?.email }
            if(currentUserRating != null){
                ratingBar.rating = currentUserRating.rating
            }
        }
    }

    fun showImages(){
        val listener = this
        async(UI) {
            recyclerViewImages.adapter = ImageAdapter(presenter.app.images.findAll().filter { it.hillfortId == presenter.hillfort.id }, listener)
            recyclerViewImages.adapter?.notifyDataSetChanged()
        }
    }

    override fun onResume() {
        super.onResume()
        mapView2.onResume()
        presenter.doResartLocationUpdates()
    }
}
