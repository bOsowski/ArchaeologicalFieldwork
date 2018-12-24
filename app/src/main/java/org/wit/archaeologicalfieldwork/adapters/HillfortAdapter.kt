package org.wit.archaeologicalfieldwork.adapters

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.card_hillfort.view.*
import org.wit.archaeologicalfieldwork.R
import org.wit.archaeologicalfieldwork.helpers.readImageFromPath
import org.wit.archaeologicalfieldwork.models.Hillfort
import java.lang.Exception

class HillfortAdapter constructor(private var hillforts: List<Hillfort>,
                                   private val listener: HillfortListener) : RecyclerView.Adapter<HillfortAdapter.MainHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        return MainHolder(LayoutInflater.from(parent.context).inflate(R.layout.card_hillfort, parent, false))
    }

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val hillfort = hillforts[holder.adapterPosition]
        holder.bind(hillfort, listener)
    }

    override fun getItemCount(): Int = hillforts.size

    class MainHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(hillfort: Hillfort,  listener : HillfortListener) {
            itemView.name.text = hillfort.name
            itemView.description.text = hillfort.description
            try{
                itemView.image.setImageBitmap(readImageFromPath(itemView.context, hillfort.images.first()))
            }catch (e: Exception){

            }
            itemView.setOnClickListener { listener.onHillfortClick(hillfort) }
        }
    }


}

interface HillfortListener {
    fun onHillfortClick(hillfort: Hillfort)
}