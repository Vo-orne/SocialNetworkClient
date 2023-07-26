package com.example.myprofile.ui.fragments

import android.Manifest
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import com.example.myprofile.utils.Constants
import com.example.myprofile.R
import com.example.myprofile.base.BaseFragment
import com.example.myprofile.databinding.FragmentMyProfileBinding
import com.example.myprofile.viewmodel.MyProfileViewModel
import com.example.myprofile.utils.ext.factory
import com.example.myprofile.utils.ext.navigateToFragment

/**
 * Fragment for displaying the user's profile.
 */
class MyProfileFragment : BaseFragment<FragmentMyProfileBinding>(FragmentMyProfileBinding::inflate) {

    /**
     * ViewModel for managing the user's profile data
     */
    private val viewModel: MyProfileViewModel by viewModels { factory() }

    /**
     * ActivityResultLauncher to request contacts permission
     */
    private val requestContactsPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                // If the permission is granted, allow access to phone contacts and navigate to the MyContactsFragment
                viewModel.allowPhoneContacts()
                navigateToFragment(R.id.action_myProfileFragment_to_myContactsFragment)
            } else {
                // If the permission is denied, show a toast with a message about the access denial
                Toast.makeText(
                    requireContext(),
                    R.string.access_is_denied,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    /**
     * Method called when the fragment's view is created
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Get the SharedPreferences instance to retrieve the user's name
        val sharedPreferences = requireContext().getSharedPreferences(
            Constants.SHARED_PREFERENCES_KEY, Context.MODE_PRIVATE
        )
        val userName = sharedPreferences.getString(Constants.USER_NAME_KEY, "")
        binding.textViewMyProfileUserName.text = userName
        setListeners()
    }

    /**
     * Method to set event listeners
     */
    override fun setListeners() {
        binding.buttonMyProfileViewMyContacts.setOnClickListener {
            // Launch the contacts permission request
            requestContactsPermission.launch(Manifest.permission.READ_CONTACTS)
        }
    }
}