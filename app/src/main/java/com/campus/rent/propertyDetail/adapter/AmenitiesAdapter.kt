package com.campus.rent.propertyDetail.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.campus.rent.R
import com.campus.rent.propertyDetail.AmenitiesModel

class AmenitiesAdapter(val context: Context, val list: List<AmenitiesModel>) :
    RecyclerView.Adapter<AmenitiesAdapter.ViewModel>() {

    inner class ViewModel(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name = itemView.findViewById<TextView>(R.id.textView13)
        val image = itemView.findViewById<ImageView>(R.id.imageView6)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewModel {
        val view = LayoutInflater.from(context).inflate(R.layout.item_amenities, parent, false)
        return ViewModel(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewModel, position: Int) {
        val n = list[position]
        holder.image.setImageResource(n.image)
        holder.name.text=n.name
    }
}