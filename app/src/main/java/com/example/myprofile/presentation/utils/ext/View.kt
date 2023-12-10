package com.example.myprofile.presentation.utils.ext

import android.view.View


fun View.visibleIf(
    condition: Boolean,
) {
    if (condition) this.visible() else this.gone()
}

fun View.visible() {
    this.visibility = View.VISIBLE
}

fun View.invisible() {
    this.visibility = View.INVISIBLE
}

fun View.gone() {
    this.visibility = View.GONE
}

