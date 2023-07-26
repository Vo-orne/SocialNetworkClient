package com.example.myprofile.ui.fragments

import android.os.Bundle
import android.view.View
import com.bumptech.glide.Glide
import com.example.myprofile.R
import com.example.myprofile.base.BaseFragment
import com.example.myprofile.data.Contact
import com.example.myprofile.databinding.FragmentDetailViewBinding
import com.example.myprofile.utils.ext.navigateToFragment

class DetailViewFragment :
    BaseFragment<FragmentDetailViewBinding>(FragmentDetailViewBinding::inflate) {

    private lateinit var contact: Contact

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        displayContactData()
        setListeners()
    }

    private fun displayContactData() {
        arguments?.let {
            contact = DetailViewFragmentArgs.fromBundle(it).contact
        }

        with(binding) {
            textViewDetailViewContactName.text = contact.name
            textViewDetailViewContactCareer.text = contact.career
            textViewDetailViewContactHomeAddress.text = contact.address
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
    }

    override fun setListeners() {
        binding.buttonDetailViewBack.setOnClickListener {
            navigateToFragment(R.id.action_detailViewFragment_to_myContactsFragment)
        }
    }
}