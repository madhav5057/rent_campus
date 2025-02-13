package com.campus.rent.mainActivity.fragments.publish.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.campus.rent.R

class GridAdapter(private val context: Context, private val items: List<Pair<String, Int>>) : BaseAdapter() {

    override fun getCount(): Int = items.size

    override fun getItem(position: Int): Any = items[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View = convertView ?: LayoutInflater.from(context).inflate(R.layout.item_accessories, parent, false)
        val imageView: ImageView = view.findViewById(R.id.imageView)
        val textView: TextView = view.findViewById(R.id.textView10)

        val (text, imageRes) = items[position]
        textView.text = text
        imageView.setImageResource(imageRes)


        return view
    }
}
