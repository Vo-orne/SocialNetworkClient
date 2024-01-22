package com.example.myprofile.presentation.utils.ext

import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.myprofile.R
import com.google.android.material.snackbar.Snackbar


fun Fragment.navigateToFragment(idAction: Int) {
    findNavController().navigate( // Responsible for the transition to the next fragment
        idAction, // id of the next fragment action, taken from nav_graph.xml
        null,
        NavOptions.Builder()
            .setEnterAnim(R.anim.slide_in_right) // Responsible for setting the login animation
            .setExitAnim(R.anim.slide_out_left) // Responsible for setting the exit animation
            .build()
    )
}

fun Fragment.navigateToFragment(action: NavDirections) {
    findNavController().navigate(
        action,
        NavOptions.Builder()
            .setEnterAnim(R.anim.slide_in_right)
            .setExitAnim(R.anim.slide_out_left)
            .build()
    )
}

fun Fragment.navigateToFragmentWithoutReturning(idAction: Int, idStartingFragment: Int) {
    findNavController().navigate( // Responsible for the transition to the next fragment
        idAction, // id of the next fragment action, taken from nav_graph.xml
        null,
        NavOptions.Builder()
            .setEnterAnim(R.anim.slide_in_right) // Responsible for setting the login animation
            .setExitAnim(R.anim.slide_out_left) // Responsible for setting the exit animation
            .setPopUpTo(
                idStartingFragment,
                true
            ) // Responsible for making it impossible to return to the signUpFragment
            .build()
    )
}

/**
 * Extension function to show a Snackbar with a message, an action, and a callback for the action.
 * @param messageResId Resource ID of the message text.
 * @param actionTextResId Resource ID of the action button text.
 * @param callback Callback to be executed when the action button is clicked.
 */
fun Fragment.showSnackbarWithAction(
    @StringRes messageResId: Int,
    @StringRes actionTextResId: Int,
    callback: () -> Unit?
) {
    Snackbar.make(
        requireView(),
        messageResId,
        Snackbar.LENGTH_LONG
    ).setAction(actionTextResId) {
        callback.invoke()
    }.show() // Automatically close the Snackbar after 5 seconds
}