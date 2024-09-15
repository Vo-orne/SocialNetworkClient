package com.example.myprofile.presentation.ui.fragments.auth.sign_up

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.example.myprofile.R
import com.example.myprofile.presentation.ui.base.BaseFragment
import com.example.myprofile.databinding.FragmentSignUpBinding
import com.example.myprofile.presentation.utils.ext.navigateToFragment
import dagger.hilt.android.AndroidEntryPoint

/**
 * Fragment for registering a new user.
 */
@AndroidEntryPoint
class SignUpFragment : BaseFragment<FragmentSignUpBinding>(FragmentSignUpBinding::inflate) {

    /**
     * ViewModel to manage the data used in this fragment.
     */
    private val viewModel: SignUpViewModel by viewModels()

    /**
     * Sets up the event listeners for the UI components.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListeners()
    }

    /**
     * Method to set event listeners for the UI components.
     * This includes setting a click listener for the register button.
     */
    override fun setListeners() {
        binding.buttonSignUpRegister.setOnClickListener {
            with(viewModel) {
                // Get the entered data: email and password
                email.value = binding.textInputEditTextSignUpEmail.text.toString()
                password.value = binding.textInputEditTextSignUpPassword.text.toString()

                // Check if the checkbox is checked and validate email and password
                if (binding.checkBoxSignUpMemberInputDate.isChecked) {
                    isCheckBoxChecked = true
                    comeToNextFragment(
                        isValidEmail() && isValidPassword() == null
                    )
                } else {
                    comeToNextFragment(
                        isValidEmail() && isValidPassword() == null
                    )
                }
            }
        }
    }

    /**
     * Method to navigate to the next fragment if the given condition is met.
     * If the condition is not met, it displays error messages for invalid email or password.
     * @param condition Boolean value representing whether to navigate to the next fragment.
     */
    private fun comeToNextFragment(
        condition: Boolean
    ) {
        if (condition) {
            // Create action to navigate to the next fragment with email and password arguments
            val action = SignUpFragmentDirections.actionSignUpFragmentToSignUpExtendedFragment(
                email = binding.textInputEditTextSignUpEmail.text.toString(),
                password = binding.textInputEditTextSignUpPassword.text.toString()
            )
            navigateToFragment(action)
        } else {
            with(binding) {
                // Display error messages if email or password is invalid
                textInputLayoutSignUpEmail.error =
                    if (viewModel.isValidEmail()) getString(R.string.error_on_email) else null

                textInputLayoutSignUpPassword.error =
                    viewModel.isValidPassword()
            }
        }
    }
}
