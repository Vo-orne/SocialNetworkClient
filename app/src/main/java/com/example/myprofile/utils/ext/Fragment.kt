package com.example.myprofile.utils.ext

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavDirections
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.myprofile.App
import com.example.myprofile.R
import com.example.myprofile.viewmodel.MyProfileViewModel
import com.example.myprofile.viewmodel.AddContactViewModel
import com.example.myprofile.viewmodel.ContactsViewModel

class ViewModelFactory(
    private val app: App
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val viewModel = when (modelClass) {
            ContactsViewModel::class.java -> {
                ContactsViewModel(app.contactsRepository)
            }

            AddContactViewModel::class.java -> {
                AddContactViewModel(app.contactsRepository)
            }

            MyProfileViewModel::class.java -> {
                MyProfileViewModel(app.contactsRepository)
            }

            else -> {
                throw IllegalStateException("Unknown view model class")
            }
        }

        return viewModel as T
    }
}

fun Fragment.factory() = ViewModelFactory(requireContext().applicationContext as App)

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