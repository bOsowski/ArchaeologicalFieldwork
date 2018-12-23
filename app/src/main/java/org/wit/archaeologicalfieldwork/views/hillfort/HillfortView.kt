package org.wit.archaeologicalfieldwork.views.hillfort

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_hillfort.*
import org.jetbrains.anko.*
import org.wit.archaeologicalfieldwork.R
import org.wit.archaeologicalfieldwork.activities.NotesActivity
import org.wit.archaeologicalfieldwork.adapters.ImageListener

class HillfortView : AppCompatActivity(), ImageListener, AnkoLogger {

    private lateinit var presenter: HillfortPresenter

    override fun onImageClick(image: String) {
        presenter.doEditImage(image)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hillfort)
//        toolbarAdd.title = title
//        setSupportActionBar(toolbarAdd)
        val layoutManager = LinearLayoutManager(this)
        recyclerViewImages.layoutManager = layoutManager
        presenter = HillfortPresenter(this)

        cancel.setOnClickListener{
            presenter.doCancel()
        }

        visitedCheckBox.setOnClickListener {
            presenter.doSetVisited()
        }

        btnSave.setOnClickListener {
            presenter.doAddOrSave()
        }

        chooseImage.setOnClickListener {
            presenter.doSelectImage()
        }

        hillfortLocation.setOnClickListener {
            presenter.doSetLocation()
        }

        delete.setOnClickListener {
            presenter.doDelete()
        }

        viewNotes.setOnClickListener {
            info("View notes clicked.")
            startActivity(intentFor<NotesActivity>())
        }
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        presenter.doActivityResult(requestCode, data)
    }
}
