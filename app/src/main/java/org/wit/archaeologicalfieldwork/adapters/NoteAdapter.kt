package org.wit.archaeologicalfieldwork.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.card_note.view.*
import org.wit.archaeologicalfieldwork.R
import org.wit.archaeologicalfieldwork.models.Note
import org.wit.archaeologicalfieldwork.models.User

class NoteAdapter constructor(private var notes: List<Note>, private var users: List<User>,
                              private val listener: NoteListener) : RecyclerView.Adapter<NoteAdapter.MainHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        return MainHolder(LayoutInflater.from(parent.context).inflate(R.layout.card_note, parent, false))
    }

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val note = notes[holder.adapterPosition]
        holder.bind(note, users, listener)
    }

    override fun getItemCount(): Int = notes.size

    class MainHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(note: Note, users: List<User>,  listener : NoteListener) {
            itemView.noteAddedOn.text = "Added on ${note.creationDate}"
            itemView.noteEditedOn.text = "Last edited on ${note.lastEdited}"
            itemView.noteAddedBy.text = "Added by ${users.find { it.id == note.userId }?.email}"
            itemView.noteText.text = note.text
            itemView.setOnClickListener { listener.onNoteClick(note) }
        }
    }
}

interface NoteListener {
    fun onNoteClick(note: Note)
}