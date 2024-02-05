package com.example.myprofile.presentation.ui.fragments.detail_view

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.navArgs
import com.example.myprofile.presentation.ui.base.BaseFragment
import com.example.myprofile.data.model.Contact
import com.example.myprofile.databinding.FragmentDetailViewBinding
import com.example.myprofile.presentation.utils.ext.loadImage
import dagger.hilt.android.AndroidEntryPoint

/**
 * Fragment for displaying contact details
 */
@AndroidEntryPoint
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
            imageViewDetailViewContactAvatar.loadImage(contact.avatar)
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