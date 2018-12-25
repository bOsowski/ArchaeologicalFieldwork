package org.wit.archaeologicalfieldwork.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager
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
import androidx.appcompat.app.AlertDialog
import android.widget.EditText
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import org.jetbrains.anko.intentFor
import org.wit.archaeologicalfieldwork.views.hillfort.HillfortView
import java.util.*


class NotesActivity : AppCompatActivity(), NoteListener, AnkoLogger {

    lateinit var app: MainApp
    lateinit var hillfort: Hillfort

    override fun onNoteClick(note: Note) {
        if(app.user?.email != note.addedBy){
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
            async(UI) {
                showNotes(app.notes.findAll().filter { it.hillfortId == hillfort.id })
            }
        }

        alert.setNegativeButton("Delete") { _, _ ->
            async(UI) {
                app.notes.delete(note)
                showNotes(app.notes.findAll().filter { it.hillfortId == hillfort.id })
            }
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
        async(UI) {
            showNotes(app.notes.findAll().filter { it.hillfortId == hillfort.id })
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.note_add -> {
                var note = Note()
                note.addedBy = app.user?.email!!
                async(UI) {
                    app.notes.create(note)
                    showNotes(app.notes.findAll().filter { it.hillfortId == hillfort.id })
                }
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
        val listener = this
        async(UI) {
            recyclerViewNotes.adapter = NoteAdapter(notes, listener)
            recyclerViewNotes.adapter?.notifyDataSetChanged()
        }
    }

}
