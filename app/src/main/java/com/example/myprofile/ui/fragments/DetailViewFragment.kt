package com.example.myprofile.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.myprofile.R
import com.example.myprofile.base.BaseFragment
import com.example.myprofile.data.Contact
import com.example.myprofile.databinding.FragmentDetailViewBinding

/**
 * Fragment for displaying contact details
 */
class DetailViewFragment :
    BaseFragment<FragmentDetailViewBinding>(FragmentDetailViewBinding::inflate) {

    /**
     * Object to hold the selected contact
     *
     */
    private val args: DetailViewFragmentArgs by navArgs()
    private val contact: Contact by lazy {
        args.contact
    }



    /**
     * Method called after the fragment's view is created
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Display the data of the selected contact
        displayContactData()

        // Set event listeners
        setListeners()
    }

    /**
     * Private method to display contact data on the screen
     */
    private fun displayContactData() {
        // Get the selected contact from the passed arguments



        // Populate the relevant fields with the received contact data
        with(binding) {
            textViewDetailViewContactName.text = contact.name
            textViewDetailViewContactCareer.text = contact.career
            textViewDetailViewContactHomeAddress.text = contact.address

            // Load the contact's avatar using Glide
            if (contact.avatar.isNotBlank()) {
                Glide.with(imageViewDetailViewContactAvatar.context)// TODO: change
                    .load(contact.avatar)
                    .circleCrop()
                    .placeholder(R.drawable.default_user_photo)
                    .error(R.drawable.default_user_photo)
                    .into(imageViewDetailViewContactAvatar)
            } else {
                // Set the app's default icon if the avatar is absent
                imageViewDetailViewContactAvatar.setImageResource(R.drawable.default_user_photo)
            }
        }
    }

    /**
     * Overridden method to set event listeners
     */
    override fun setListeners() {
        binding.buttonDetailViewBack.setOnClickListener {
            // Navigate back to the previous page in the ViewPager2
            navController.navigateUp()
        }
    }
}