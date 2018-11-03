package org.wit.archaeologicalfieldwork.activities

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_notes.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.startActivityForResult
import org.wit.archaeologicalfieldwork.R
import org.wit.archaeologicalfieldwork.adapters.NoteAdapter
import org.wit.archaeologicalfieldwork.adapters.NoteListener
import org.wit.archaeologicalfieldwork.main.MainApp
import org.wit.archaeologicalfieldwork.models.Hillfort
import org.wit.archaeologicalfieldwork.models.Note
import android.content.DialogInterface
import android.support.v7.app.AlertDialog
import android.widget.EditText
import kotlinx.android.synthetic.main.activity_hillfort.view.*
import kotlinx.android.synthetic.main.card_note.*
import java.util.*


class NotesActivity : AppCompatActivity(), NoteListener, AnkoLogger {

    lateinit var app: MainApp

    override fun onNoteClick(note: Note) {
        val alert = AlertDialog.Builder(this)

        alert.setTitle("Edit Node")

        val input = EditText(this)
        input.setText(note.text)
        alert.setView(input)

        alert.setPositiveButton("Save") { _, _ ->
            note.text = input.text.toString()
            note.lastEdited = Date()
            info("Set text to ${note.text}")
            showNotes(app.currentFort.notes)
        }

        alert.setNegativeButton("Delete") { _, _ ->
            app.currentFort.notes.remove(note)
            showNotes(app.currentFort.notes)
        }
        alert.show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notes)
        app = application as MainApp
        setSupportActionBar(toolbarNotes)

        val layoutManager = LinearLayoutManager(this)
        recyclerViewNotes.layoutManager = layoutManager

        showNotes(app.currentFort.notes)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.note_add -> {
                var note = Note()
                note.userId = app.currentUser.id
                app.currentFort.notes.add(note)
                showNotes(app.currentFort.notes)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.notes_add, menu)
        return super.onCreateOptionsMenu(menu)
    }

    fun showNotes(notes: List<Note>){
        recyclerViewNotes.adapter = NoteAdapter(notes, app.users.findAll(), this)
        recyclerViewNotes.adapter?.notifyDataSetChanged()
    }

}
