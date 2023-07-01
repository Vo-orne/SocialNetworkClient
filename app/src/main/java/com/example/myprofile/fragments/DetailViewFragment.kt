package com.example.myprofile.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.myprofile.R
import com.example.myprofile.databinding.FragmentDetailViewBinding
import com.example.myprofile.utils.navigateToFragment

class DetailViewFragment : Fragment() {
    private var _binding: FragmentDetailViewBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentDetailViewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setListeners()
    }

    private fun setListeners() {
        binding.buttonDetailViewBack.setOnClickListener {
            navigateToFragment(R.id.action_detailViewFragment_to_myContactsFragment)
        }
    }


}