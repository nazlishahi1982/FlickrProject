package com.omadahealthchallenge.flickrphotos.data.remote

import com.omadahealthchallenge.flickrphotos.data.ApiHelper

class FlickrRepository(private val apiHelper: ApiHelper) {

    suspend fun getRecentPhotos(pageNumber: Int) = apiHelper.getRecentPhotos(pageNumber)

    suspend fun getPhotosBySearchTerm(text: String, pageNumber: Int) = apiHelper.getPhotosBySearchTerm(text, pageNumber)
}