package com.omadahealthchallenge.flickrphotos.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.omadahealthchallenge.flickrphotos.R
import com.omadahealthchallenge.flickrphotos.data.Photo
import com.squareup.picasso.Picasso

class PhotoAdapter(val listener: OnPhotoClickListener): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val photoList = mutableListOf<Photo>()

    fun populate(list: List<Photo>) {
        photoList.addAll(list)
        notifyItemRangeInserted(0, photoList.size)
    }

    fun addMoreItems(list: List<Photo>) {
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
        val viewHolder = holder as ViewHolder
        val photo = photoList[position]
        viewHolder.bind(photo)
        viewHolder.itemView.setOnClickListener {
            listener.onPhotoClicked(photo)
        }
    }

    private class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        fun bind(photo: Photo) {
            val imageView = itemView.findViewById<ImageView>(R.id.photoView)

            Picasso.get()
                .load(photo.makeUrl())
                .into(imageView)
        }
    }
}