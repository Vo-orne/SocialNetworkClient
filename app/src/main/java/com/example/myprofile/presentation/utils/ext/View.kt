package com.example.myprofile.presentation.utils.ext

import android.view.View

/**
 * Extension function to set the visibility of a view based on a condition.
 * @param condition Boolean value that determines if the view should be visible.
 */
fun View.visibleIf(condition: Boolean) {
    if (condition) this.visible() else this.gone()
}

/**
 * Extension function to make a view visible.
 */
fun View.visible() {
    this.visibility = View.VISIBLE
}

/**
 * Extension function to make a view invisible.
 */
fun View.invisible() {
    this.visibility = View.INVISIBLE
}

/**
 * Extension function to make a view gone.
 */
fun View.gone() {
    this.visibility = View.GONE
}
