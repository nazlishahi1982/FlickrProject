package com.omadahealthchallenge.flickrphotos.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.omadahealthchallenge.flickrphotos.R
import com.squareup.picasso.Picasso

class PhotoAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val photoList = mutableListOf<String>()
    private var recyclerView: RecyclerView? = null

    fun populate(list: List<String>) {
        photoList.addAll(list)
        notifyItemRangeInserted(0, photoList.size)
    }

    fun addMoreItems(list: List<String>) {
        photoList.addAll(list)
        notifyDataSetChanged()
    }


    fun clear() {
        val previousItemCount = itemCount
        photoList.clear()
        notifyItemRangeRemoved(0, previousItemCount)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(inflater.inflate(R.layout.view_photo_list_item, parent, false))
    }

    override fun getItemCount(): Int = photoList.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ViewHolder).bind(photoList[position])
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        this.recyclerView = recyclerView
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        this.recyclerView = null
    }

    private class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        fun bind(url: String) {
            val imageView = itemView.findViewById<ImageView>(R.id.photoView)

            Picasso.get()
                .load(url)
                .into(imageView)
        }
    }
}