package com.app.movieflix.utilities

import android.widget.ImageView
import com.app.movieflix.imageLoader.ImageLoader
import com.bumptech.glide.Glide

object Utility {

    enum class LibType {
        GLIDE, CUSTOM
    }

    fun getRating(rating: Float, outOf: Float): Float {
        val percentage = (rating * 100) / outOf
        return (percentage * 5) / 100
    }

    fun getCachingType() : LibType {
        return LibType.CUSTOM
    }

    fun loadImage(libType: LibType, imageView: ImageView, url: String) {
        if (libType == LibType.GLIDE) {
            Glide.with(imageView.context)
                .load(url)
                .into(imageView)
        } else if (libType == LibType.CUSTOM) {
            ImageLoader.displayImage(url, imageView)
        }
    }

}