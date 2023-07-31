package com.omadahealthchallenge.flickrphotos.data

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object RetrofitBuilder {

    private const val BASE_URL = "https://www.flickr.com/services/rest/"

    private var httpLoggingInterceptor = HttpLoggingInterceptor()
        .setLevel(HttpLoggingInterceptor.Level.BODY)

    private var okHttpClient = OkHttpClient.Builder()
        .addInterceptor(httpLoggingInterceptor)
        .build()

    private var retrofit: Retrofit? = null

    val client: Retrofit?
        get() {
            if(retrofit == null) {
                val gson = GsonBuilder()
                    .serializeNulls()
                    .serializeSpecialFloatingPointValues()
                    .setLenient()
                    .create()

                retrofit = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build()
            }
            return retrofit
        }
}