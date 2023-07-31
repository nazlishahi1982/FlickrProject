package com.omadahealthchallenge.flickrphotos.ui

import com.omadahealthchallenge.flickrphotos.data.Photo

interface OnPhotoClickListener {
    fun onPhotoClicked(photo: Photo)
}