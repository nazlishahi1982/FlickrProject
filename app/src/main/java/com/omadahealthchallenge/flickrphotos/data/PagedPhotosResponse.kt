package com.omadahealthchallenge.flickrphotos.data

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class PagedPhotosResponse(
    @SerializedName("page") val page: String,
    @SerializedName("pages") val pages: String,
    @SerializedName("perpage") val perpage: String,
    @SerializedName("total") val total: String,
    @SerializedName("photo") val photoList: List<Photo>
): Serializable