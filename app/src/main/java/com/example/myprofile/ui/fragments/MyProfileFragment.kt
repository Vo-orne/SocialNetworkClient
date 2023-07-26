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

class MyProfileFragment : BaseFragment<FragmentMyProfileBinding>(FragmentMyProfileBinding::inflate) {

    private val viewModel: MyProfileViewModel by viewModels { factory() }

    private val requestContactsPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                viewModel.allowPhoneContacts()
                navigateToFragment(R.id.action_myProfileFragment_to_myContactsFragment)
            } else {
                Toast.makeText(
                    requireContext(),
                    R.string.access_is_denied,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sharedPreferences = requireContext().getSharedPreferences(
            Constants.SHARED_PREFERENCES_KEY, Context.MODE_PRIVATE
        )
        val userName = sharedPreferences.getString(Constants.USER_NAME_KEY, "")
        binding.textViewMyProfileUserName.text = userName
        setListeners()
    }

    override fun setListeners() {
        binding.buttonMyProfileViewMyContacts.setOnClickListener {
            requestContactsPermission.launch(Manifest.permission.READ_CONTACTS)
        }
    }
}