package com.campus.rent.search.models

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.campus.rent.R

class PlacesAdapter(
    private var places: List<PlacesModel>,
    private val onItemClick: (PlacesModel) -> Unit
) : RecyclerView.Adapter<PlacesAdapter.ViewHolder>() {


    fun updateData(newPlaces: List<PlacesModel>) {
        places = newPlaces
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val placeName: TextView = itemView.findViewById(R.id.placeName)
        val placeDescription: TextView = itemView.findViewById(R.id.placeDescription)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_location, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return places.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val place = places[position]
        holder.placeName.text = place.placeName
        holder.placeDescription.text = place.placeDescription
        holder.itemView.setOnClickListener { onItemClick(place) }

    }


}