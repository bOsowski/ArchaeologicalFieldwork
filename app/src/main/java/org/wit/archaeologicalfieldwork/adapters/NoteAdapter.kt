package org.wit.archaeologicalfieldwork.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.card_note.view.*
import org.wit.archaeologicalfieldwork.R

class NoteAdapter constructor(private var texts: List<String>,
                               private val listener: TextListener) : RecyclerView.Adapter<NoteAdapter.MainHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        return MainHolder(LayoutInflater.from(parent.context).inflate(R.layout.card_note, parent, false))
    }

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val text = texts[holder.adapterPosition]
        holder.bind(text, listener)
    }

    override fun getItemCount(): Int = texts.size

    class MainHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(text: String,  listener : TextListener) {
            //itemView.editText.setText(text)
            //itemView.setOnClickListener { listener.onTextClick(text) }
        }
    }
}

interface TextListener {
    fun onTextClick(text: String)
}