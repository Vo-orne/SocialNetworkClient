package com.example.myprofile.ui.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.example.myprofile.utils.Constants
import com.example.myprofile.R
import com.example.myprofile.base.BaseFragment
import com.example.myprofile.databinding.FragmentSignUpBinding
import com.example.myprofile.utils.ext.navigateToFragmentWithoutReturning
import java.util.*

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
        viewModel.registerLiveData.observe(viewLifecycleOwner) {
            if (it) {
                navigateToFragmentWithoutReturning(
                    R.id.action_signUpFragment_to_pagerFragment, R.id.signUpFragment
                )
            }
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
                viewModel.saveAutoLogin(newEmail, newPassword)
                comeToNextFragment(
                    viewModel.registerLiveData.value == true,
                    newEmail,
                    editor
                )
            } else {
                comeToNextFragment(
                    viewModel.validationInputs(newEmail, newPassword),
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
            saveUserName(email, editor)
            navigateToFragmentWithoutReturning(
                R.id.action_signUpFragment_to_pagerFragment, R.id.signUpFragment
            )
        } else {
            if (viewModel.emailError != null)
                binding.textInputLayoutSignUpEmail.error = viewModel.emailError

            if (viewModel.passwordError != null)
                binding.textInputLayoutSignUpPassword.error = viewModel.passwordError
        }
    }

    /**
     * Private method to save the user's name from the entered email
     */
    private fun saveUserName(email: String, editor: SharedPreferences.Editor) {
        val userName: String = parsEmail(email)
        editor.putString(Constants.USER_NAME_KEY, userName)
        editor.apply()
    }

    /**
     * Private method to parse the entered email and create the user's name from it
     */
    private fun parsEmail(email: String): String {
        val substring = email.substring(0, email.indexOf('@')) // qwe.rty@d.asd -> qwe.rty
        val splittedEmail = substring.split('.') // qwe.rty -> [qwe, rty]

        if (splittedEmail.size == 1) {
            return splittedEmail[0]
        }
        val sb = StringBuilder()
        splittedEmail.forEach { it ->
            val word =
                it.replaceFirstChar {
                    if (it.isLowerCase())
                        it.titlecase(Locale.ROOT)
                    else
                        it.toString()
                }
            sb.append("$word ")
        }
        return sb.substring(0, sb.length - 1).toString()
    }
}