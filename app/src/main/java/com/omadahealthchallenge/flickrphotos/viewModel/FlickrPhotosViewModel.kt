package com.omadahealthchallenge.flickrphotos.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omadahealthchallenge.flickrphotos.data.PagedPhotosResponse
import com.omadahealthchallenge.flickrphotos.data.Photo
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

        if (currentPageNumber == 1) {
            photosDisplayState.postValue(PhotosDisplayState.PopulateFirstPagePhotos(pagedPhotosResponse.photoList))
        } else {
            photosDisplayState.postValue(PhotosDisplayState.PopulateSubsequentPagePhotos(pagedPhotosResponse.photoList))
        }
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
        data class PopulateFirstPagePhotos(val photoList: List<Photo>): PhotosDisplayState()
        data class PopulateSubsequentPagePhotos(val photoList: List<Photo>): PhotosDisplayState()
        data class Error(val errorMessage: String?): PhotosDisplayState()
    }
}