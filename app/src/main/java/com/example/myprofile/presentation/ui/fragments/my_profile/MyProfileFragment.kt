package com.example.myprofile.presentation.ui.fragments.my_profile

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.example.myprofile.R
import com.example.myprofile.databinding.FragmentMyProfileBinding
import com.example.myprofile.presentation.ui.base.BaseFragment
import com.example.myprofile.presentation.ui.fragments.pager.PagerFragment
import com.example.myprofile.presentation.ui.fragments.pager.adapter.utils.ViewPagerFragments
import com.example.myprofile.presentation.utils.ext.navigateToFragment
import dagger.hilt.android.AndroidEntryPoint

/**
 * Fragment for displaying the user's profile.
 */
@AndroidEntryPoint
class MyProfileFragment : BaseFragment<FragmentMyProfileBinding>(FragmentMyProfileBinding::inflate) {

    /**
     * ViewModel for managing the user's profile data
     */
    private val viewModel: MyProfileViewModel by viewModels()

    /**
     * Called after the fragment view is created.
     * Methods are called here to display user data and set event listeners.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUserData()
        setListeners()
    }

    /**
     * Populates text fields with user data obtained from the ViewModel.
     */
    private fun setUserData() {
        with(binding) {
            textViewMyProfileUserName.text = viewModel.getUserName()
            textViewMyProfileUserCareer.text = viewModel.getUserCareer()
            textViewMyProfileUserHomeAddress.text = viewModel.getUserAddress()
        }
    }

    /**
     * Sets event listeners for buttons:
     *
     * buttonMyProfileViewMyContacts: When clicked, changes the current ViewPager2 element
     * to a contact fragment.
     * MyContactsFragment is assumed to be at index 1 in ViewPager2.
     *
     * buttonMyProfileEditProfile: Navigate to the edit profile snippet.
     *
     * buttonMyProfileLogOut: Clear user data on logout and navigate to the login screen.
     */
    override fun setListeners() {
        with(binding) {
            buttonMyProfileViewMyContacts.setOnClickListener {
                (parentFragment as PagerFragment).getViewPager().currentItem =
                    ViewPagerFragments.CONTACTS_FRAGMENT.ordinal
            }
            buttonMyProfileEditProfile.setOnClickListener {
                navigateToFragment(R.id.action_pagerFragment_to_editProfileFragment)
            }
            buttonMyProfileLogOut.setOnClickListener {
                viewModel.clearUserData()
                navigateToFragment(R.id.action_pagerFragment_to_logInFragment)
            }
        }
    }
}
