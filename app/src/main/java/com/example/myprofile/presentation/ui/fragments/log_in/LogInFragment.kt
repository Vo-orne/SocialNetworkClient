package com.example.myprofile.presentation.ui.fragments.log_in

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.example.myprofile.R
import com.example.myprofile.databinding.FragmentLogInBinding
import com.example.myprofile.domain.ApiState
import com.example.myprofile.presentation.ui.base.BaseFragment
import com.example.myprofile.presentation.utils.ext.log
import com.example.myprofile.presentation.utils.ext.navigateToFragment
import com.example.myprofile.presentation.utils.ext.navigateToFragmentWithoutReturning
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LogInFragment: BaseFragment<FragmentLogInBinding>(FragmentLogInBinding::inflate) {

    private val viewModel: LogInViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.autoLogin()

        setListeners()
        setObservers()
    }

    private fun setObservers() {
        lifecycleScope.launch {
            viewModel.loginState.flowWithLifecycle(viewLifecycleOwner.lifecycle).collect {
                when (it) {
                    is ApiState.Success<*> -> {
                        navigateToFragmentWithoutReturning(
                            R.id.action_logInFragment_to_pagerFragment,
                            0
                        )
                    }

                    is ApiState.Loading -> {
                        log("Loading")
                    }

                    is ApiState.Initial -> {
                        log("Initial")
                    }

                    is ApiState.Error -> {
                        log(it.error)
                    }
                }
            }
        }
    }

    override fun setListeners() {
        with(binding) {
            buttonLogInLogin.setOnClickListener {
                loginUser()
            }
            textViewLogInSignUp.setOnClickListener {
                navigateToFragment(R.id.action_logInFragment_to_signUpFragment)
            }
        }
    }

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