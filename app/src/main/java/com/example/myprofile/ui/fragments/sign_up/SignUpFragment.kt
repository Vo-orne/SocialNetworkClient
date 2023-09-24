package com.example.myprofile.ui.fragments.sign_up

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.example.myprofile.utils.Constants
import com.example.myprofile.R
import com.example.myprofile.ui.base.BaseFragment
import com.example.myprofile.databinding.FragmentSignUpBinding
import com.example.myprofile.utils.ext.navigateToFragmentWithoutReturning

/**
 * Fragment for registering a new user.
 */
class SignUpFragment : BaseFragment<FragmentSignUpBinding>(FragmentSignUpBinding::inflate) {

    private val sharedPreferences: SharedPreferences by lazy {
        requireContext().getSharedPreferences(
            Constants.SHARED_PREFERENCES_KEY, Context.MODE_PRIVATE
        )
    }
    private val viewModel: SignUpViewModel by viewModels()
    // TODO: view model?
    /**
     * Method called when the fragment's view is created
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Get an instance of SharedPreferences for data storage
        // Check if previously saved user data exists for automatic login
        accountAutoLogin()
        // Set event listeners
        setListeners()
    }

    /**
     * Private method for automatic login if user data is previously saved
     */
    private fun accountAutoLogin() {
        if (sharedPreferences.getBoolean(Constants.AUTO_LOGIN_KEY, false)) {
            navigateToFragmentWithoutReturning(
                R.id.action_signUpFragment_to_pagerFragment, R.id.signUpFragment
            )
        }
    }

    /**
     * Method to set event listeners
     */
    override fun setListeners() {
        binding.buttonSignUpRegister.setOnClickListener {
            // Get the entered data: email and password
            val newEmail = binding.textInputEditTextSignUpEmail.text.toString()
            val newPassword = binding.textInputEditTextSignUpPassword.text.toString()
            val editor = sharedPreferences.edit()

            if (binding.checkBoxSignUpMemberInputDate.isChecked) {
                viewModel.saveAutoLogin(newEmail, newPassword, editor)
                comeToNextFragment(
                    viewModel.registerLiveData.value == true,
                    newEmail,
                    editor
                )
            } else {
                comeToNextFragment(
                    viewModel.isValidEmail(newEmail) && viewModel.isValidPassword(newPassword) == null,
                    newEmail,
                    editor
                )
            }
        }
    }

    private fun comeToNextFragment(
        condition: Boolean,
        email: String,
        editor: SharedPreferences.Editor
    ) {
        if (condition) {
            viewModel.saveUserName(email, editor)
            navigateToFragmentWithoutReturning(
                R.id.action_signUpFragment_to_pagerFragment, R.id.signUpFragment
            )
        } else {
            with(binding) {
                textInputLayoutSignUpEmail.error =
                    if (viewModel.isValidEmail(textInputEditTextSignUpEmail.text.toString())) getString(R.string.error_on_email) else null

                textInputLayoutSignUpPassword.error =
                    viewModel.isValidPassword(textInputEditTextSignUpPassword.text.toString())
            }
        }
    }
}