package com.omadahealthchallenge.flickrphotos.ui

import android.app.Dialog
import android.content.Context
import android.graphics.Rect
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import androidx.recyclerview.widget.RecyclerView.OnScrollListener
import com.google.android.material.snackbar.Snackbar
import com.omadahealthchallenge.flickrphotos.FlickrPhotosApplication
import com.omadahealthchallenge.flickrphotos.R
import com.omadahealthchallenge.flickrphotos.data.Photo
import com.omadahealthchallenge.flickrphotos.databinding.ActivityMainBinding
import com.omadahealthchallenge.flickrphotos.viewModel.FlickrPhotosViewModel
import com.omadahealthchallenge.flickrphotos.viewModel.FlickrPhotosViewModelFactory
import com.squareup.picasso.Picasso

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var viewModel: FlickrPhotosViewModel

    private var photoAdapter: PhotoAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initViews()

        viewModel = ViewModelProvider(
            this,
            FlickrPhotosViewModelFactory(FlickrPhotosApplication.getApiHelperInstance())
        )[FlickrPhotosViewModel::class.java]

        initObservers()

        viewModel.getRecentPhotos()
    }

    private fun initViews() {
        photoAdapter = PhotoAdapter(object: OnPhotoClickListener {
            override fun onPhotoClicked(photo: Photo) {
                showPhotoDetailsDialog(photo)
            }
        })

        with (binding.photosGridView) {
            val spaceBetweenItems = resources.getDimensionPixelSize(R.dimen.margin_small)
            addItemDecoration(object: ItemDecoration() {
                override fun getItemOffsets(
                    outRect: Rect,
                    view: View,
                    parent: RecyclerView,
                    state: RecyclerView.State
                ) {
                    with(outRect) {
                        right = spaceBetweenItems
                        bottom = spaceBetweenItems
                    }
                }
            })
            adapter = photoAdapter
        }

        binding.searchEditText.setOnEditorActionListener { textView, action, keyEvent ->
            if (action == EditorInfo.IME_ACTION_SEARCH ||
                (keyEvent != null && keyEvent.action == KeyEvent.ACTION_UP && keyEvent.keyCode == KeyEvent.KEYCODE_ENTER)) {
                hideKeyboard()
                photoAdapter?.clear()
                viewModel.getInitialPhotos(textView.text)
            }
            return@setOnEditorActionListener true
        }

        binding.photosGridView.addOnScrollListener(object: OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_IDLE) {
                    viewModel.getMorePhotos(binding.searchEditText.text.toString())
                }
            }
        })
    }

    private fun initObservers() {
        viewModel.photosDisplayState.observe(this) { state ->
            when (state) {
                is FlickrPhotosViewModel.PhotosDisplayState.PopulateFirstPagePhotos -> {
                    photoAdapter?.populate(state.photoList)
                }
                is FlickrPhotosViewModel.PhotosDisplayState.PopulateSubsequentPagePhotos -> {
                    photoAdapter?.addMoreItems(state.photoList)
                }
                is FlickrPhotosViewModel.PhotosDisplayState.Error -> {
                    Snackbar.make(
                        binding.root,
                        state.errorMessage ?: getString(R.string.generic_error_message),
                        Snackbar.LENGTH_LONG
                    ).show()
                }
            }
        }

        viewModel.progressBarState.observe(this) {
            binding.progressBar.isVisible = it
        }
    }

    private fun hideKeyboard() {
        val manager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        if (currentFocus != null) {
            manager.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
        }
    }

    private fun showPhotoDetailsDialog(photo: Photo) {
        val dialog = Dialog(this@MainActivity)
        val view = layoutInflater.inflate(R.layout.view_enlarged_image, binding.root, false)
        view.findViewById<TextView>(R.id.photoTitleView).text = photo.title
        view.findViewById<TextView>(R.id.photoIdView).text = photo.id
        view.findViewById<TextView>(R.id.photoOwnerView).text = photo.owner
        Picasso.get()
            .load(photo.makeUrl())
            .into(view.findViewById<ImageView>(R.id.enlargedImageView))

        dialog.setContentView(view)
        dialog.show()
    }
}