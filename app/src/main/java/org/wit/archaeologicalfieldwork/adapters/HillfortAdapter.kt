package org.wit.archaeologicalfieldwork.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.card_hillfort.view.*
import org.wit.archaeologicalfieldwork.R
import org.wit.archaeologicalfieldwork.models.Hillfort
import org.wit.archaeologicalfieldwork.models.Image


class HillfortAdapter constructor(private var hillforts: List<Hillfort>, private var images: List<Image>,
                                  private val listener: HillfortListener) : RecyclerView.Adapter<HillfortAdapter.MainHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        return MainHolder(LayoutInflater.from(parent.context).inflate(R.layout.card_hillfort, parent, false))
    }

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val hillfort = hillforts[holder.adapterPosition]
        val images = images.filter{it.hillfortId == hillfort.id}
        var image: Image? = null
        if(!images.isEmpty()){
            image = images.first()
        }
        holder.bind(hillfort, image, listener)
    }

    override fun getItemCount(): Int = hillforts.size

    class MainHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(hillfort: Hillfort, image: Image?, listener : HillfortListener) {
            itemView.name.text = hillfort.name
            itemView.description.text = hillfort.description
            if(image != null){
                Glide.with(itemView).load(image.data).into(itemView.image)

                //itemView.image.setImageBitmap(readImageFromPath(itemView.context, image.data))
            }
            itemView.setOnClickListener { listener.onHillfortClick(hillfort) }
        }
    }
}

interface HillfortListener {
    fun onHillfortClick(hillfort: Hillfort)
}