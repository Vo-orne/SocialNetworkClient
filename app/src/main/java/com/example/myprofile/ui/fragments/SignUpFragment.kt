package com.example.myprofile.ui.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Patterns
import android.view.View
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

    private lateinit var sharedPreferences: SharedPreferences

    /**
     * Method called when the fragment's view is created
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Get an instance of SharedPreferences for data storage
        sharedPreferences = requireContext().getSharedPreferences(
            Constants.SHARED_PREFERENCES_KEY, Context.MODE_PRIVATE
        )
        // Check if previously saved user data exists for automatic login
        accountAutoLogin()
        // Set event listeners
        setListeners()
    }

    /**
     * Private method for automatic login if user data is previously saved
     */
    private fun accountAutoLogin() {
        val email = sharedPreferences.getString(Constants.EMAIL_KEY, "") ?: ""
        if (email.isNotEmpty()) {
            // Navigate to the "MyProfileFragment" without the ability to return back
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

            // Validate the entered data
            if (validateInputs(newEmail, newPassword)) {
                // If the "Remember Me" checkbox is checked, clear previously saved data
                if (binding.checkBoxSignUpMemberInputDate.isChecked) {
                    editor.clear()
                    editor.apply()
                    // Save the entered email and password
                    saveLoginData(newEmail, newPassword, editor)
                }
                // Save the user as a new user with the entered email
                saveUserName(newEmail, editor)
                // Navigate to the "MyProfileFragment" without the ability to return back
                navigateToFragmentWithoutReturning(
                    R.id.action_signUpFragment_to_pagerFragment, R.id.signUpFragment
                )
            }
        }
    }

    /**
     * Private method to validate the entered data
     */
    private fun validateInputs(emailInput: String, passwordInput: String): Boolean {
        var isValid = true

        // Validate the entered email
        if (!Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()) {
            isValid = false
            binding.textInputLayoutSignUpEmail.error = getString(R.string.error_on_email)
        } else {
            binding.textInputLayoutSignUpEmail.error = null
        }

        // Validate the entered password
        if (isValidPassword(passwordInput) == Constants.PASSWORD_IS_CORRECT) {
            binding.textInputLayoutSignUpPassword.error = null
        } else {
            isValid = false
            binding.textInputLayoutSignUpPassword.error = isValidPassword(passwordInput)
        }

        return isValid
    }

    /**
     * Private method to validate the entered password
     */
    private fun isValidPassword(password: String): String {
        var isHasEnoughLength = false
        var isHasNumber = false
        var isHasLetter = false

        if (password.length >= Constants.MAX_PASSWORD_SIZE) {
            isHasEnoughLength = true
        }

        if (password.contains(Regex("\\d+"))) isHasNumber = true
        if (password.contains(Regex("[a-zA-Z]+"))) isHasLetter = true

        return when {
            !isHasEnoughLength -> getString(R.string.error_password_is_short)
            !isHasNumber -> getString(R.string.error_password_without_numbers)
            !isHasLetter -> getString(R.string.error_password_without_letters)
            else -> Constants.PASSWORD_IS_CORRECT
        }
    }

    /**
     * Private method to save the entered user data (email and password)
     */
    private fun saveLoginData(email: String, password: String, editor: SharedPreferences.Editor) {
        editor.putString(Constants.EMAIL_KEY, email)
        editor.putString(Constants.PASSWORD_KEY, password)
        editor.apply()
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