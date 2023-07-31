package com.omadahealthchallenge.flickrphotos.data

class ApiHelper (private val flickrRestService: FlickrRestService) {

    suspend fun getRecentPhotos(pageNumber: Int) = flickrRestService.getRecentPhotos(pageNumber)

    suspend fun getPhotosBySearchTerm(text: String, pageNumber: Int) = flickrRestService.getPhotosBySearchTerm(text, pageNumber)
}