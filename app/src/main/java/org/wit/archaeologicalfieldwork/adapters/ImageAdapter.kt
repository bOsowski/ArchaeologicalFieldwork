package org.wit.archaeologicalfieldwork.adapters

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.card_image.view.*
import org.wit.archaeologicalfieldwork.R
import org.wit.archaeologicalfieldwork.models.Image

class ImageAdapter constructor(private var images: List<Image>,
                                  private val listener: ImageListener) : RecyclerView.Adapter<ImageAdapter.MainHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        return MainHolder(LayoutInflater.from(parent.context).inflate(R.layout.card_image, parent, false))
    }

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val image = images[holder.adapterPosition]
        holder.bind(image, listener)
    }

    override fun getItemCount(): Int = images.size

    class MainHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(image: Image,  listener : ImageListener) {
            Glide.with(itemView).load(image.data).into(itemView.image)
            itemView.setOnClickListener { listener.onImageClick(image) }
        }
    }
}

interface ImageListener {
    fun onImageClick(image: Image)
}