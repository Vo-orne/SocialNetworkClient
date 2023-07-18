package com.example.myprofile.fragments

import android.Manifest
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.myprofile.Constants
import com.example.myprofile.R
import com.example.myprofile.databinding.FragmentMyProfileBinding
import com.example.myprofile.main.MyProfileViewModel
import com.example.myprofile.utils.factory
import com.example.myprofile.utils.navigateToFragment

class MyProfileFragment : Fragment() {
    private var _binding: FragmentMyProfileBinding? = null
    private val binding get() = _binding!!

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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMyProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sharedPreferences = requireContext().getSharedPreferences(
            Constants.SHARED_PREFERENCES_KEY, Context.MODE_PRIVATE
        )
        val userName = sharedPreferences.getString(Constants.USER_NAME_KEY, "")
        binding.textViewMyProfileUserName.text = userName
        navigateToMyContacts()
    }

    private fun navigateToMyContacts() {
        binding.buttonMyProfileViewMyContacts.setOnClickListener {
            requestContactsPermission.launch(Manifest.permission.READ_CONTACTS)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
