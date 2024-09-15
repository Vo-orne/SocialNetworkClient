package com.example.myprofile.presentation.utils.ext

import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.myprofile.R
import com.google.android.material.snackbar.Snackbar

/**
 * Navigates to a new fragment using a navigation action ID.
 * @param idAction The ID of the navigation action, defined in nav_graph.xml.
 */
fun Fragment.navigateToFragment(idAction: Int) {
    findNavController().navigate(
        idAction, // The navigation action ID
        null,
        NavOptions.Builder()
            .setEnterAnim(R.anim.slide_in_right) // Animation for entering the new fragment
            .setExitAnim(R.anim.slide_out_left) // Animation for exiting the current fragment
            .build()
    )
}

/**
 * Navigates to a new fragment using NavDirections.
 * @param action The NavDirections object defining the navigation action.
 */
fun Fragment.navigateToFragment(action: NavDirections) {
    findNavController().navigate(
        action,
        NavOptions.Builder()
            .setEnterAnim(R.anim.slide_in_right) // Animation for entering the new fragment
            .setExitAnim(R.anim.slide_out_left) // Animation for exiting the current fragment
            .build()
    )
}

/**
 * Navigates to a new fragment
 * and removes all previous fragments from the back stack up to the specified fragment.
 * @param idAction The ID of the navigation action, defined in nav_graph.xml.
 * @param idStartingFragment The ID of the fragment to be kept in the back stack.
 */
fun Fragment.navigateToFragmentWithoutReturning(idAction: Int, idStartingFragment: Int) {
    findNavController().navigate(
        idAction, // The navigation action ID
        null,
        NavOptions.Builder()
            .setEnterAnim(R.anim.slide_in_right) // Animation for entering the new fragment
            .setExitAnim(R.anim.slide_out_left) // Animation for exiting the current fragment
            .setPopUpTo(
                idStartingFragment,
                true
            ) // Remove all fragments up to idStartingFragment from the back stack
            .build()
    )
}

/**
 * Shows a Snackbar with a message, an action button, and a callback for the action button click.
 * @param messageResId The resource ID of the message text.
 * @param actionTextResId The resource ID of the action button text.
 * @param callback The callback to be executed when the action button is clicked.
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
        callback.invoke() // Execute the callback when the action button is clicked
    }.show() // Display the Snackbar
}
