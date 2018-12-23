package org.wit.archaeologicalfieldwork.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_notes.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.wit.archaeologicalfieldwork.R
import org.wit.archaeologicalfieldwork.adapters.NoteAdapter
import org.wit.archaeologicalfieldwork.adapters.NoteListener
import org.wit.archaeologicalfieldwork.main.MainApp
import org.wit.archaeologicalfieldwork.models.Hillfort
import org.wit.archaeologicalfieldwork.models.Note
import android.support.v7.app.AlertDialog
import android.widget.EditText
import org.jetbrains.anko.intentFor
import org.wit.archaeologicalfieldwork.views.hillfort.HillfortView
import java.util.*


class NotesActivity : AppCompatActivity(), NoteListener, AnkoLogger {

    lateinit var app: MainApp
    lateinit var hillfort: Hillfort

    override fun onNoteClick(note: Note) {
        if(app.currentUser.id != note.userId){
            return
        }

        val alert = AlertDialog.Builder(this)

        alert.setTitle("Edit Node")

        val input = EditText(this)
        input.setText(note.text)
        alert.setView(input)

        alert.setPositiveButton("Save") { _, _ ->
            note.text = input.text.toString()
            note.lastEdited = Date()
            info("Set text to ${note.text}")
            showNotes(hillfort.notes)
        }

        alert.setNegativeButton("Delete") { _, _ ->
            hillfort.notes.remove(note)
            showNotes(hillfort.notes)
        }
        alert.show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notes)
        app = application as MainApp
        hillfort = intent.getParcelableExtra("hillfort_data")
        setSupportActionBar(toolbarNotes)

        val layoutManager = LinearLayoutManager(this)
        recyclerViewNotes.layoutManager = layoutManager

        showNotes(hillfort.notes)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.note_add -> {
                var note = Note()
                note.userId = app.currentUser.id
                hillfort.notes.add(note)
                showNotes(hillfort.notes)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        startActivity(intentFor<HillfortView>().putExtra("hillfort_edit", hillfort))
        finish()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.notes_add, menu)
        return super.onCreateOptionsMenu(menu)
    }

    private fun showNotes(notes: List<Note>){
        recyclerViewNotes.adapter = NoteAdapter(notes, app.data.findAll().users, app.currentUser, this)
        recyclerViewNotes.adapter?.notifyDataSetChanged()
    }

}
