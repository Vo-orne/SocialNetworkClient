package com.example.myprofile.presentation.ui.fragments.detail_view

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.navArgs
import com.example.myprofile.data.model.Contact
import com.example.myprofile.presentation.ui.base.BaseFragment
import com.example.myprofile.databinding.FragmentDetailViewBinding
import com.example.myprofile.presentation.utils.ext.loadImage
import dagger.hilt.android.AndroidEntryPoint

/**
 * Responsible for displaying contact details.
 * It uses the data passed through the navigation arguments to populate the user interface.
 */
@AndroidEntryPoint
class DetailViewFragment :
    BaseFragment<FragmentDetailViewBinding>(FragmentDetailViewBinding::inflate) {

    /**
     * An object to receive the passed fragment arguments. The selected contact is stored here.
     */
    private val args: DetailViewFragmentArgs by navArgs()

    /**
     * A local variable that is initialized with the contact data from the arguments.
     */
    private val contact: Contact by lazy {
        args.contact
    }

    /**
     * It is called after creating a fragment view.
     * This is where contact data is displayed and event handlers are set.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        displayContactData()
        setListeners()
    }

    /**
     * Fills the appropriate fields on the screen with contact data.
     * Loads a contact's avatar using the loadImage utility.
     */
    private fun displayContactData() {
        with(binding) {
            textViewDetailViewContactName.text = contact.name
            textViewDetailViewContactCareer.text = contact.career
            textViewDetailViewContactHomeAddress.text = contact.address
            imageViewDetailViewContactAvatar.loadImage(contact.avatar)
        }
    }

    /**
     * Sets event handlers for interface elements.
     * In this case, an event handler for the back button
     * that returns the user to the previous page in ViewPager2.
     */
    override fun setListeners() {
        binding.buttonDetailViewBack.setOnClickListener {
            navController.navigateUp()
        }
    }
}