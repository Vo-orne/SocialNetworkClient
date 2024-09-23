package com.example.myprofile.presentation.ui.fragments.log_in

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.example.myprofile.R
import com.example.myprofile.databinding.FragmentLogInBinding
import com.example.myprofile.domain.states.ApiState
import com.example.myprofile.presentation.ui.base.BaseFragment
import com.example.myprofile.presentation.utils.ext.invisible
import com.example.myprofile.presentation.utils.ext.log
import com.example.myprofile.presentation.utils.ext.navigateToFragment
import com.example.myprofile.presentation.utils.ext.navigateToFragmentWithoutReturning
import com.example.myprofile.presentation.utils.ext.visible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

/**
 * Responsible for user login.
 * It uses a ViewModel to handle login requests and handles progress display and navigation.
 */
@AndroidEntryPoint
class LogInFragment : BaseFragment<FragmentLogInBinding>(FragmentLogInBinding::inflate) {

    /**
     * ViewModel to manage the data used in this fragment.
     */
    private val viewModel: LogInViewModel by viewModels()

    /**
     * Progress bar to display the download process.
     */
    private lateinit var progressBar: ProgressBar

    /**
     * Called after the fragment view is created.
     * This includes initializing the progressBar, attempting automatic login,
     * and setting up listeners and observers.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        progressBar = binding.progressBar
        viewModel.autoLogin()
        setListeners()
        setObservers()
    }

    /**
     * Configures observers for LiveData loginState
     * that monitors the state of the API (Loading, Success, Initial, Error)
     * and displays the corresponding progress bar state or performs navigation.
     */
    private fun setObservers() {
        lifecycleScope.launch {
            viewModel.loginState.flowWithLifecycle(viewLifecycleOwner.lifecycle).collect { apiState ->
                when (apiState) {
                    is ApiState.Success<*> -> {
                        progressBar.invisible()
                        // Navigate to the main page and clear the back stack
                        navigateToFragmentWithoutReturning(
                            R.id.action_logInFragment_to_pagerFragment,
                            0
                        )
                    }
                    is ApiState.Loading -> {
                        progressBar.visible()
                        log("Loading")
                    }
                    is ApiState.Initial -> {
                        progressBar.invisible()
                        log("Initial")
                    }
                    is ApiState.Error -> {
                        progressBar.invisible()
                        log(apiState.error)
                    }
                }
            }
        }
    }

    /**
     * Set up event listeners for buttons and text views.
     */
    override fun setListeners() {
        with(binding) {
            buttonLogInLogin.setOnClickListener {
                loginUser()
            }
            textViewLogInSignUp.setOnClickListener {
                // Navigate to the sign-up fragment
                navigateToFragment(R.id.action_logInFragment_to_signUpFragment)
            }
        }
    }

    /**
     * Gets the data from the input fields (email and password),
     * checks if the data needs to be saved for automatic login,
     * and calls the ViewModel loginUser method to attempt the login.
     */
    private fun loginUser() {
        with(viewModel) {
            email.value = binding.textInputEditTextLogInEmail.text.toString()
            password.value = binding.textInputEditTextLogInPassword.text.toString()

            if (binding.checkBoxLogInMemberInputDate.isChecked)
                saveAutoLoginData()

            loginUser()
        }
    }
}