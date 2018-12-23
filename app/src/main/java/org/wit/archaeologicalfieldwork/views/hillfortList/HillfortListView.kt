package org.wit.archaeologicalfieldwork.views.hillfortList

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_hillfort_list.*
import org.jetbrains.anko.intentFor
import org.wit.archaeologicalfieldwork.R
import org.wit.archaeologicalfieldwork.activities.SettingsActivity
import org.wit.archaeologicalfieldwork.adapters.HillfortAdapter
import org.wit.archaeologicalfieldwork.adapters.HillfortListener
import org.wit.archaeologicalfieldwork.models.Hillfort
import org.wit.archaeologicalfieldwork.views.BaseView

class HillfortListView : BaseView(), HillfortListener {

   lateinit var presenter: HillfortListPresenter

    override fun onHillfortClick(hillfort: Hillfort) {
        presenter.doEditHillfort(hillfort)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hillfort_list)
        init(toolbarMain)

        presenter = initPresenter(HillfortListPresenter(this)) as HillfortListPresenter

        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = HillfortAdapter(presenter.getHillforts(), this)

        settings.setOnClickListener {
            startActivity(intentFor<SettingsActivity>())
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.item_add -> presenter.doAddHillfort()
            R.id.item_map -> presenter.doShowHillfortsMap()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        recyclerView.adapter?.notifyDataSetChanged()
        super.onActivityResult(requestCode, resultCode, data)
    }
}
