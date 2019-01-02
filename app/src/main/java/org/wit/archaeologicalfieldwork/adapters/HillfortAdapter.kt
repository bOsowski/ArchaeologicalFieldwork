package org.wit.archaeologicalfieldwork.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.card_hillfort.view.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.imageResource
import org.wit.archaeologicalfieldwork.R
import org.wit.archaeologicalfieldwork.models.Favourite
import org.wit.archaeologicalfieldwork.models.Hillfort
import org.wit.archaeologicalfieldwork.models.Image
import org.wit.archaeologicalfieldwork.models.Rating
import org.wit.archaeologicalfieldwork.views.hillfortList.HillfortListView


class HillfortAdapter constructor(private var hillforts: List<Hillfort>, private var images: List<Image>, private var ratings: List<Rating>, private var favourites: List<Favourite>,
                                  private val listener: HillfortListener) : RecyclerView.Adapter<HillfortAdapter.MainHolder>(), AnkoLogger {

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
        var rating = -1f
        val ratingsForHillfort = ratings.filter { it.hillfortId == hillfort.id }
        ratingsForHillfort.forEach {
            if(it.rating != -1){
                rating += it.rating
            }
        }
        if(!ratingsForHillfort.isEmpty()){
            rating /= ratingsForHillfort.size
        }
        holder.bind(hillfort, image, !favourites.filter { it.hillfortId == hillfort.id && it.addedBy == FirebaseAuth.getInstance().currentUser!!.email }.isEmpty(), rating, listener)
    }

    override fun getItemCount(): Int = hillforts.size

    class MainHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(hillfort: Hillfort, image: Image?, isFavourite: Boolean, rating: Float, listener : HillfortListener) {
            itemView.name.text = hillfort.name
            itemView.description.text = hillfort.description
            itemView.hillfortAddedBy.text = "Added by ${hillfort.addedBy}"

            if(isFavourite){
                itemView.favouriteButton.imageResource = android.R.drawable.star_big_on
            }
            else{
                itemView.favouriteButton.imageResource = android.R.drawable.star_big_off
            }
            itemView.favouriteButton.setOnClickListener{
                listener.onFavouriteClick(hillfort, itemView.favouriteButton)
            }

            if(rating > 0){
                itemView.overalRating.text = (listener as HillfortListView).resources!!.getString(R.string.rating, rating)
            }
            else{
                itemView.overalRating.text = "Not rated."
            }
            if(image != null){
                Glide.with(itemView).load(image.data).into(itemView.image)
            }

            itemView.setOnClickListener { listener.onHillfortClick(hillfort) }
        }
    }
}

interface HillfortListener {
    fun onHillfortClick(hillfort: Hillfort)
    fun onFavouriteClick(hillfort: Hillfort, favouriteButton: ImageButton)
}