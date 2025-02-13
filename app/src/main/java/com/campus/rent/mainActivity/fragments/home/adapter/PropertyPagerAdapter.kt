package com.campus.rent.mainActivity.fragments.home.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.campus.rent.R
import com.campus.rent.database.model.property.getProperty.Data
import com.campus.rent.propertyDetail.PropertyDetailActivity
import com.campus.rent.utils.Constants.TAG

class PropertyPagerAdapter(val context: Context) :
    PagingDataAdapter<Data, PropertyPagerAdapter.PropertyViewHolder>(
        COMPARATOR
    ) {


    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: PropertyViewHolder, position: Int) {

        val item = getItem(position)
        if (item != null) {
            holder.title.text = item.title
            holder.gender.text = item.gender
            holder.type.text = item.property_type
            holder.price.text = item.price
            Glide.with(context).load(item.images[0]).placeholder(R.drawable.ii).into(holder.image)

            holder.itemView.setOnClickListener {
                item?.property_id?.let { id ->
                    val intent = Intent(context, PropertyDetailActivity::class.java)
                    intent.putExtra("property_id", id.toInt())
                    Log.d(TAG, id)
                    context.startActivity(intent)
                } ?: run {
                    Log.e("PropertyPagerAdapter", "Property ID is null for item at position $position")
                }
            }


        }



    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PropertyViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_property, parent, false)

        return PropertyViewHolder(view)
    }

    class PropertyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.title)
        val type: TextView = itemView.findViewById(R.id.property_type)
        val gender = itemView.findViewById<TextView>(R.id.gender)
        val price = itemView.findViewById<TextView>(R.id.price)
        val image = itemView.findViewById<ImageView>(R.id.img)
    }

    companion object {
        private val COMPARATOR = object : DiffUtil.ItemCallback<Data>() {
            override fun areItemsTheSame(oldItem: Data, newItem: Data): Boolean {
                return oldItem.property_id == newItem.property_id
            }

            override fun areContentsTheSame(oldItem: Data, newItem: Data): Boolean {
                return oldItem == newItem
            }

        }
    }


}