package com.example.myprofile.presentation.ui.fragments.edit_profile

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.example.myprofile.R
import com.example.myprofile.databinding.FragmentEditUserBinding
import com.example.myprofile.domain.ApiState
import com.example.myprofile.presentation.ui.base.BaseFragment
import com.example.myprofile.presentation.ui.fragments.edit_profile.dialog.CalendarDialogFragment
import com.example.myprofile.presentation.ui.fragments.edit_profile.interfaces.DialogCalendarListener
import com.example.myprofile.presentation.utils.Constants
import com.example.myprofile.presentation.utils.Parser
import com.example.myprofile.presentation.utils.ext.invisible
import com.example.myprofile.presentation.utils.ext.log
import com.example.myprofile.presentation.utils.ext.navigateToFragment
import com.example.myprofile.presentation.utils.ext.visible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

/**
 * Dialog fragment for adding a new contact.
 */
@AndroidEntryPoint
class EditProfileFragment :
    BaseFragment<FragmentEditUserBinding>(FragmentEditUserBinding::inflate) {

    /**
     * ViewModel for handling the addition of new contacts
     */
    private val viewModel: EditProfileViewModel by viewModels()
    private lateinit var progressBar: ProgressBar

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        progressBar = binding.progressBar
        setListeners()
        setObservers()
        setCalendar()
    }

    /**
     * Set up event listeners for buttons and ImageView
     */
    override fun setListeners() {
        // Handle click on the "Save" button
        with(binding) {
            buttonEditUserSave.setOnClickListener {
                saveContact()
            }
            // Handle click on the "Back" button
            buttonEditUserBack.setOnClickListener {
                navigateToFragment(R.id.action_editProfileFragment_to_pagerFragment)
            }
        }
    }

    private fun setObservers() {
        lifecycleScope.launch {
            viewModel.userStateFlow.observe(viewLifecycleOwner, Observer { apiState ->
                when (apiState) {
                    is ApiState.Success<*> -> {
                        progressBar.invisible()
                        viewModel.setUserData()
                        navigateToFragment(R.id.action_editProfileFragment_to_pagerFragment)
                    }

                    is ApiState.Error -> {
                        progressBar.invisible()
                        log(apiState.error)
                    }

                    is ApiState.Initial -> {
                        progressBar.invisible()
                        log(apiState)
                    }

                    is ApiState.Loading -> {
                        progressBar.visible()
                        log(apiState)
                    }
                }
            })
        }
    }

    /**
     * Method to save the newly added contact
     */
    private fun saveContact() {
        with(binding) {
            val name = textInputEditTextEditUserUsername.text.toString()
            val phone = textInputEditTextEditUserPhone.text.toString()
            val address = textInputEditTextEditUserAddress.text.toString()
            val career = textInputEditTextEditUserCareer.text.toString()
            val birthday =
                Parser.getDataFromString(textInputEditTextEditUserDateOfBirth.text.toString())
            viewModel.editUser(name, phone, address, career, birthday)
        }
    }

    private fun setCalendar() {
        with(binding) {
            textInputEditTextEditUserDateOfBirth.setOnClickListener {
                val dialog = CalendarDialogFragment()
                dialog.setListener(listener = object : DialogCalendarListener {
                    override fun onDateSelected(date: String) {
                        textInputEditTextEditUserDateOfBirth.setText(date)
                    }
                })
                dialog.show(parentFragmentManager, Constants.EDIT_USER_DIALOG)
            }
        }
    }
}