package com.example.myprofile.fragments

import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.myprofile.Constants
import com.example.myprofile.R
import com.example.myprofile.databinding.FragmentMyProfileBinding
import com.example.myprofile.utils.navigateToFragment

class MyProfileFragment : Fragment() {
    private var _binding: FragmentMyProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
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
        setListeners()
    }

    private fun setListeners() {
        navigateToMyContacts()
    }

    private fun navigateToMyContacts() {
        binding.buttonMyProfileViewMyContacts.setOnClickListener {
            val permission = android.Manifest.permission.READ_CONTACTS
            val requestCode = 1

            if (ContextCompat.checkSelfPermission(requireContext(), permission) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(requireActivity(), arrayOf(permission), requestCode)
            } else {
                navigateToFragment(R.id.action_myProfileFragment_to_myContactsFragment)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}