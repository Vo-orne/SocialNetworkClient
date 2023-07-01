package com.example.myprofile.utils

import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.myprofile.R

fun Fragment.navigateToFragment(idAction: Int){
    findNavController().navigate( // Responsible for the transition to the next fragment
        idAction, // id of the next fragment action, taken from nav_graph.xml
        null,
        NavOptions.Builder()
            .setEnterAnim(R.anim.slide_in_right) // Responsible for setting the login animation
            .setExitAnim(R.anim.slide_out_left) // Responsible for setting the exit animation
            .build()
    )
}

fun Fragment.navigateToFragmentWithoutReturning(idAction: Int, idStartingFragment: Int){
    findNavController().navigate( // Responsible for the transition to the next fragment
        idAction, // id of the next fragment action, taken from nav_graph.xml
        null,
        NavOptions.Builder()
            .setEnterAnim(R.anim.slide_in_right) // Responsible for setting the login animation
            .setExitAnim(R.anim.slide_out_left) // Responsible for setting the exit animation
            .setPopUpTo(idStartingFragment, true) // Responsible for making it impossible to return to the signUpFragment
            .build()
    )
}