package com.example.myprofile.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.myprofile.R
import com.example.myprofile.databinding.FragmentDetailViewBinding
import com.example.myprofile.mycontacts.Contact
import com.example.myprofile.utils.navigateToFragment

class DetailViewFragment : Fragment() {

    private var _binding: FragmentDetailViewBinding? = null
    private val binding get() = _binding!!

    private lateinit var contact: Contact

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentDetailViewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            contact = DetailViewFragmentArgs.fromBundle(it).contact
        }

        with(binding) {
            textViewDetailViewContactName.text = contact.name
            textViewDetailViewContactCareer.text = contact.career
            if (contact.avatar.isNotBlank()) {
                Glide.with(imageViewDetailViewContactAvatar.context)
                    .load(contact.avatar)
                    .circleCrop()
                    .placeholder(R.drawable.default_user_photo)
                    .error(R.drawable.default_user_photo)
                    .into(imageViewDetailViewContactAvatar)
            } else {
                imageViewDetailViewContactAvatar.setImageResource(R.drawable.default_user_photo)
            }
        }
        setListeners()
    }

    private fun setListeners() {
        binding.buttonDetailViewBack.setOnClickListener {
            navigateToFragment(R.id.action_detailViewFragment_to_myContactsFragment)
        }
    }
}