package com.campus.rent.propertyDetail.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.campus.rent.databinding.ItemPropertyImageBinding

class PropertyImagesAdapter(val list: ArrayList<String>) :
    RecyclerView.Adapter<PropertyImagesAdapter.PropertyImageViewHolder>() {


    class PropertyImageViewHolder(val binding: ItemPropertyImageBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PropertyImageViewHolder {
        val binding = ItemPropertyImageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PropertyImageViewHolder(binding)  }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: PropertyImageViewHolder, position: Int) {
        Glide.with(holder.itemView.context)
            .load(list[position])
            .into(holder.binding.imageView3)    }

}