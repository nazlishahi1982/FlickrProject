package com.omadahealthchallenge.flickrphotos

import android.app.Application
import com.omadahealthchallenge.flickrphotos.data.ApiHelper
import com.omadahealthchallenge.flickrphotos.data.FlickrRestService
import com.omadahealthchallenge.flickrphotos.data.RetrofitBuilder.client

class FlickrPhotosApplication: Application() {

    companion object {
        private var mApiHelper: ApiHelper? = null

        fun getApiHelperInstance(): ApiHelper {
            if(mApiHelper==null){
                mApiHelper = ApiHelper(client!!.create(FlickrRestService::class.java))
            }
            return mApiHelper!!
        }
    }
}