package com.app.movieflix.core

import android.app.Application
import com.app.movieflix.imageLoader.DoubleCache
import com.app.movieflix.imageLoader.ImageLoader
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class AppController : Application() {

    override fun onCreate() {
        super.onCreate()
        ImageLoader.setCache(DoubleCache(applicationContext))
    }

}