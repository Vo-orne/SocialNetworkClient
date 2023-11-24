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

    private val viewModel: SignUpViewModel by viewModels()

    /**
     * Method called when the fragment's view is created
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Get an instance of SharedPreferences for data storage
        // Check if previously saved user data exists for automatic login
        //accountAutoLogin()
        // Set event listeners
        setListeners()
    }

//    /**
//     * Private method for automatic login if user data is previously saved
//     */
//    private fun accountAutoLogin() {
//        if (sharedPreferences.getBoolean(Constants.AUTO_LOGIN_KEY, false)) {
//            navigateToFragmentWithoutReturning(
//                R.id.action_signUpFragment_to_pagerFragment, R.id.signUpFragment
//            )
//        }
//    }

    /**
     * Method to set event listeners
     */
    override fun setListeners() {
        binding.buttonSignUpRegister?.setOnClickListener {
            // Get the entered data: email and password
            viewModel.email.value = binding.textInputEditTextSignUpEmail.text.toString()
            viewModel.password.value = binding.textInputEditTextSignUpPassword.text.toString()

            if (binding.checkBoxSignUpMemberInputDate.isChecked) {
                //viewModel.saveAutoLogin(newEmail, newPassword, editor)
                comeToNextFragment(
                    viewModel.registerLiveData.value == true
                )
            } else {
                comeToNextFragment(
                    viewModel.isValidEmail() && viewModel.isValidPassword() == null
                )
            }
        }
    }

    private fun comeToNextFragment(
        condition: Boolean
    ) {
        if (condition) {
            navigateToFragment(R.id.action_signUpFragment_to_signUpExtendedFragment)
        } else {
            with(binding) {
                textInputLayoutSignUpEmail.error =
                    if (viewModel.isValidEmail()) getString(R.string.error_on_email) else null

                textInputLayoutSignUpPassword.error =
                    viewModel.isValidPassword()
            }
        }
    }
}