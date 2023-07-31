package com.omadahealthchallenge.flickrphotos.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.omadahealthchallenge.flickrphotos.data.ApiHelper
import com.omadahealthchallenge.flickrphotos.data.remote.FlickrRepository
import java.lang.IllegalArgumentException

@Suppress("UNCHECKED_CAST")
class FlickrPhotosViewModelFactory(private val apiHelper: ApiHelper) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(FlickrPhotosViewModel::class.java)){
            return FlickrPhotosViewModel(FlickrRepository(apiHelper)) as T
        }
        throw IllegalArgumentException("Class not found")
    }
}