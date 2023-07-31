package com.omadahealthchallenge.flickrphotos.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omadahealthchallenge.flickrphotos.data.PagedPhotosResponse
import com.omadahealthchallenge.flickrphotos.data.remote.FlickrRepository
import kotlinx.coroutines.launch

class FlickrPhotosViewModel(private val repo: FlickrRepository): ViewModel() {

    val photosDisplayState = MutableLiveData<PhotosDisplayState>()
    val progressBarState = MutableLiveData<Boolean>()

    private var totalNumberOfPages: Int = 0
    private var currentPageNumber: Int = 0

    fun getRecentPhotos() {
        viewModelScope.launch {
            progressBarState.postValue(true)
            runCatching {
                val result = repo.getRecentPhotos(currentPageNumber + 1)
                onPhotosAvailable(result.photos)
            }.onFailure {
                photosDisplayState.postValue(PhotosDisplayState.Error(it.localizedMessage))
            }
            progressBarState.postValue(false)
        }
    }

    private fun searchForPhotosByTerm(searchTerm: String) {
        viewModelScope.launch {
            progressBarState.postValue(true)
            runCatching {
                val result = repo.getPhotosBySearchTerm(searchTerm, currentPageNumber + 1)
                onPhotosAvailable(result.photos)
            }.onFailure {
                photosDisplayState.postValue(PhotosDisplayState.Error(it.localizedMessage))
            }
            progressBarState.postValue(false)
        }
    }

    private fun onPhotosAvailable(pagedPhotosResponse: PagedPhotosResponse) {
        totalNumberOfPages = pagedPhotosResponse.pages.toInt()
        currentPageNumber = pagedPhotosResponse.page.toInt()

        val photoUrlList = pagedPhotosResponse.photoList.map { photo ->
            makePhotoUrl(photo.server, photo.id, photo.secret)
        }

        if (currentPageNumber == 1) {
            photosDisplayState.postValue(PhotosDisplayState.PopulateFirstPagePhotos(photoUrlList))
        } else {
            photosDisplayState.postValue(PhotosDisplayState.PopulateSubsequentPagePhotos(photoUrlList))
        }
    }

    private fun makePhotoUrl(serverId: String, photoId: String, secret: String): String {
        return "https://live.staticflickr.com/$serverId/${photoId}_${secret}.jpg"
    }

    fun getMorePhotos(text: CharSequence) {
        if ((currentPageNumber + 1) <= totalNumberOfPages) {
            lookupPhotos(text)
        }
    }

    fun getInitialPhotos(text: CharSequence) {
        currentPageNumber = 0
        totalNumberOfPages = 0

        lookupPhotos(text)
    }

    private fun lookupPhotos(text: CharSequence) {
        if (text.isEmpty()) {
            getRecentPhotos()
        } else {
            searchForPhotosByTerm(text.toString())
        }
    }

    sealed class PhotosDisplayState {
        data class PopulateFirstPagePhotos(val photoList: List<String>): PhotosDisplayState()
        data class PopulateSubsequentPagePhotos(val photoList: List<String>): PhotosDisplayState()
        data class Error(val errorMessage: String?): PhotosDisplayState()
    }
}