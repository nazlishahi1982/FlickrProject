package com.omadahealthchallenge.flickrphotos.data

import retrofit2.http.GET
import retrofit2.http.Query

interface FlickrRestService {

    companion object {
        private const val COMMON_PARAMS = "?api_key=a0222db495999c951dc33702500fdc4d&format=json&nojsoncallback=1&method="
    }

    @GET("${COMMON_PARAMS}flickr.photos.getRecent")
    suspend fun getRecentPhotos(@Query("page") page: Int): PhotosResponse

    @GET("${COMMON_PARAMS}flickr.photos.search")
    suspend fun getPhotosBySearchTerm(@Query("text") searchTerm: String, @Query("page") pageNumber: Int): PhotosResponse
}