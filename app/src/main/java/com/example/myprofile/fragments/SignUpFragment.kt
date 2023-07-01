package com.example.myprofile.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.myprofile.Constants
import com.example.myprofile.R
import com.example.myprofile.databinding.FragmentSignUpBinding
import com.example.myprofile.utils.navigateToFragmentWithoutReturning
import java.util.*

class SignUpFragment : Fragment() {
    private var _binding: FragmentSignUpBinding? = null
    private val binding get() = _binding!!
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentSignUpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedPreferences = requireContext().getSharedPreferences(
            Constants.SHARED_PREFERENCES_KEY, Context.MODE_PRIVATE
        )
        accountAutoLogin()
        setListeners()
    }

    private fun accountAutoLogin() {
        val email = sharedPreferences.getString(Constants.EMAIL_KEY, "") ?: ""
        if (email.isNotEmpty()) {
            navigateToFragmentWithoutReturning(
                R.id.action_signUpFragment_to_myProfileFragment, R.id.signUpFragment
            )
        }
    }

    private fun parsEmail(email: String): String {
        val substring = email.substring(0, email.indexOf('@')) // qwe.rty@d.asd -> qwe.rty
        val splittedEmail = substring.split('.') // qwe.rty -> [qwe, rty]

        if (splittedEmail.size == 1) {
            return splittedEmail[0]
        }
        val sb = StringBuilder()
        splittedEmail.forEach {
            val word = it.capitalize(Locale.ROOT)
            sb.append("$word ")
        }
        return sb.substring(0, sb.length - 1).toString()
    }

    private fun setListeners() {
        binding.buttonSignUpRegister.setOnClickListener {
            val newEmail = binding.textInputEditTextSignUpEmail.text.toString()
            val newPassword = binding.textInputEditTextSignUpPassword.text.toString()
            val editor = sharedPreferences.edit()

            if (validateInputs(newEmail, newPassword)) {
                if (binding.checkBoxSignUpMemberInputDate.isChecked) {
                    editor.clear()
                    editor.apply()
                    saveLoginData(newEmail, newPassword, editor)
                }
                saveUserName(newEmail, editor)
                navigateToFragmentWithoutReturning(
                    R.id.action_signUpFragment_to_myProfileFragment, R.id.signUpFragment
                )
            }
        }
    }

    private fun validateInputs(emailInput: String, passwordInput: String): Boolean {
        var isValid = true

        if (!Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()) {
            isValid = false
            binding.textInputLayoutSignUpEmail.error = getString(R.string.error_on_email)
        } else {
            binding.textInputLayoutSignUpEmail.error = null
        }

        if (isValidPassword(passwordInput) == Constants.PASSWORD_IS_CORRECT) {
            binding.textInputLayoutSignUpPassword.error = null
        } else {
            isValid = false
            binding.textInputLayoutSignUpPassword.error = isValidPassword(passwordInput)
        }

        return isValid
    }

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

    private fun saveLoginData(email: String, password: String, editor: SharedPreferences.Editor) {
        editor.putString(Constants.EMAIL_KEY, email)
        editor.putString(Constants.PASSWORD_KEY, password)
        editor.apply()
    }

    private fun saveUserName(email: String, editor: SharedPreferences.Editor) {
        val userName: String = parsEmail(email)
        editor.putString(Constants.USER_NAME_KEY, userName)
        editor.apply()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
