package org.wit.archaeologicalfieldwork.adapters

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.card_note.view.*
import org.wit.archaeologicalfieldwork.R
import org.wit.archaeologicalfieldwork.helpers.simplifyDate
import org.wit.archaeologicalfieldwork.models.Note
import java.util.*

class NoteAdapter constructor(private var notes: List<Note>, private val listener: NoteListener) : RecyclerView.Adapter<NoteAdapter.MainHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        return MainHolder(LayoutInflater.from(parent.context).inflate(R.layout.card_note, parent, false))
    }

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val note = notes[holder.adapterPosition]
        holder.bind(note, listener)
    }

    override fun getItemCount(): Int = notes.size

    class MainHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(note: Note, listener : NoteListener) {
            itemView.noteAddedOn.text = "Added on ${simplifyDate(Date(note.creationDate))}}"
            itemView.noteEditedOn.text = "Last edited on ${simplifyDate(Date(note.lastEdited))}"
            itemView.noteAddedBy.text = "Added by ${note.addedBy}"
            if(note.addedBy == FirebaseAuth.getInstance().currentUser?.email) {
                itemView.setBackgroundResource(R.color.colorGray)
            }
            itemView.noteText.text = note.text
            itemView.setOnClickListener { listener.onNoteClick(note) }
        }
    }
}

interface NoteListener {
    fun onNoteClick(note: Note)
}