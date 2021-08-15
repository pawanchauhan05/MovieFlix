package com.app.movieflix.utilities

import android.view.View

fun View.visibility(flag: Boolean): Unit {
    val toBeChanged = if (flag) {
        View.VISIBLE
    } else {
        View.GONE
    }
    if (visibility != toBeChanged) {
        visibility = toBeChanged
    }

}