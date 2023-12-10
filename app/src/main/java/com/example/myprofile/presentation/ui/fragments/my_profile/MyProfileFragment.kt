package com.example.myprofile.presentation.ui.fragments.my_profile

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.example.myprofile.databinding.FragmentMyProfileBinding
import com.example.myprofile.presentation.ui.base.BaseFragment
import com.example.myprofile.presentation.ui.fragments.pager.PagerFragment
import com.example.myprofile.presentation.ui.fragments.pager.adapter.utils.ViewPagerFragments
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

//    /**
//     * ActivityResultLauncher to request contacts permission
//     */
//    private val requestContactsPermission =
//        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
//            if (isGranted) {
//                // If the permission is granted, allow access to phone contacts and navigate to the MyContactsFragment
//                viewModel.allowPhoneContacts()
//            } else {
//                // If the permission is denied, show a toast with a message about the access denial
//                Toast.makeText(
//                    requireContext(),
//                    R.string.access_is_denied,
//                    Toast.LENGTH_SHORT
//                ).show()
//            }
//        }

    /**
     * Method called when the fragment's view is created
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.textViewMyProfileUserName.text = viewModel.getUserName()
//        requestContactsPermission.launch(Manifest.permission.READ_CONTACTS)
        setListeners()
    }

    /**
     * Method to set event listeners
     */
    override fun setListeners() {
        // Add a click listener for the button to switch to MyContactsFragment
        binding.buttonMyProfileViewMyContacts.setOnClickListener {
            // Get the reference to ViewPager2 from PagerFragment
            (parentFragment as PagerFragment).getViewPager().currentItem = ViewPagerFragments.CONTACTS_FRAGMENT.ordinal
            // Switch to MyContactsFragment by setting the current item of the ViewPager2
             // Assuming MyContactsFragment is at index 1
        }
    }
}