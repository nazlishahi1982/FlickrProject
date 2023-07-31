package com.omadahealthchallenge.flickrphotos.data

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class PhotosResponse(
    @SerializedName("photos") val photos: PagedPhotosResponse
): Serializable