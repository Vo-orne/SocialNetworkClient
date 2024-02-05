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
     * Method called when the fragment's view is created
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUserData()
        setListeners()
    }

    private fun setUserData() {
        with(binding) {
            textViewMyProfileUserName.text = viewModel.getUserName()
            textViewMyProfileUserCareer.text = viewModel.getUserCareer()
            textViewMyProfileUserHomeAddress.text = viewModel.getUserAddress()
        }
    }

    /**
     * Method to set event listeners
     */
    override fun setListeners() {
        with(binding) {
            buttonMyProfileViewMyContacts.setOnClickListener {
                // Get the reference to ViewPager2 from PagerFragment
                (parentFragment as PagerFragment).getViewPager().currentItem =
                    ViewPagerFragments.CONTACTS_FRAGMENT.ordinal
                // Switch to MyContactsFragment by setting the current item of the ViewPager2
                // Assuming MyContactsFragment is at index 1
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