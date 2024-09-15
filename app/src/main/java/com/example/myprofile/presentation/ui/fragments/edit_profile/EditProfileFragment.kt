package com.example.myprofile.presentation.ui.fragments.edit_profile

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.example.myprofile.R
import com.example.myprofile.databinding.FragmentEditUserBinding
import com.example.myprofile.domain.states.ApiState
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
 * Responsible for editing the user profile.
 * It provides an interface for entering and saving user data
 * and integrates with a date selection dialog.
 */
@AndroidEntryPoint
class EditProfileFragment :
    BaseFragment<FragmentEditUserBinding>(FragmentEditUserBinding::inflate) {

    /**
     * ViewModel to manage the data used in this fragment.
     */
    private val viewModel: EditProfileViewModel by viewModels()

    /**
     * Progress bar to display the download process.
     */
    private lateinit var progressBar: ProgressBar

    /**
     * Called after creating a fragment view.
     * Configures the progress bar, events and watchers, and the calendar.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        progressBar = binding.progressBar
        setListeners()
        setObservers()
        setCalendar()
    }

    /**
     * Configures event handlers for buttons in the fragment:
     *
     * "Save" button: Saves user profile data.
     * "Back" button: Returns to the previous fragment (PagerFragment).
     */
    override fun setListeners() {
        with(binding) {
            buttonEditUserSave.setOnClickListener {
                saveContact()
            }
            buttonEditUserBack.setOnClickListener {
                navigateToFragment(R.id.action_editProfileFragment_to_pagerFragment)
            }
        }
    }

    /**
     * Configures observers for ViewModel state,
     * handling various API states (Loading, Success, Error, Initial).
     */
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
     * Collects data from input fields and calls a ViewModel method to save changes.
     */
    private fun saveContact() {
        with(binding) {
            val name = textInputEditTextEditUserUsername.text.toString()
            val phone = textInputEditTextEditUserPhone.text.toString()
            val address = textInputEditTextEditUserAddress.text.toString()
            val career = textInputEditTextEditUserCareer.text.toString()
            val birthday = Parser.getDataFromString(textInputEditTextEditUserDateOfBirth.text.toString())
            viewModel.editUser(name, phone, address, career, birthday)
        }
    }

    /**
     * Configures the date selection dialog.
     * When selecting a date, it updates the input field with the date.
     */
    private fun setCalendar() {
        with(binding) {
            textInputEditTextEditUserDateOfBirth.setOnClickListener {
                val dialog = CalendarDialogFragment()
                dialog.setListener(object : DialogCalendarListener {
                    override fun onDateSelected(date: String) {
                        textInputEditTextEditUserDateOfBirth.setText(date)
                    }
                })
                dialog.show(parentFragmentManager, Constants.EDIT_USER_DIALOG)
            }
        }
    }
}