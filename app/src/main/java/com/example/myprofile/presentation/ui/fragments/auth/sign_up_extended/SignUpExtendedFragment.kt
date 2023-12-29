package com.example.myprofile.presentation.ui.fragments.auth.sign_up_extended

import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.example.myprofile.R
import com.example.myprofile.databinding.FragmentSingUpExtendedBinding
import com.example.myprofile.domain.ApiState
import com.example.myprofile.presentation.ui.base.BaseFragment
import com.example.myprofile.presentation.ui.fragments.auth.sign_up.SignUpViewModel
import com.example.myprofile.presentation.utils.ext.log
import com.example.myprofile.presentation.utils.ext.navigateToFragment
import com.example.myprofile.presentation.utils.ext.navigateToFragmentWithoutReturning
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SignUpExtendedFragment :
    BaseFragment<FragmentSingUpExtendedBinding>(FragmentSingUpExtendedBinding::inflate) {

    private val viewModel: SignUpViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListeners()
        setObservers()
    }

    private fun setObservers() {
        lifecycleScope.launch {
            viewModel.registerState.flowWithLifecycle(viewLifecycleOwner.lifecycle).collect {
                when (it) {
                    is ApiState.Success<*> -> {
                        log(it.data.toString())
                        navigateToFragmentWithoutReturning(
                            R.id.action_signUpExtendedFragment_to_pagerFragment,
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
            buttonSignUpExtendedForward.setOnClickListener {
                if (viewModel.isCheckBoxChecked)
                    viewModel.saveAutoLoginData()
                registerUser()
            }
            buttonSignUpExtendedCancel.setOnClickListener {
                navigateToFragment(R.id.action_signUpExtendedFragment_to_signUpFragment)
            }
        }
    }

    private fun registerUser() {
        with(viewModel) {
            name.value = binding.textInputEditTextSignUpExtendedName.text.toString()
            phone.value = binding.textInputEditTextSignUpExtendedPhone.text.toString()
            registerUser()
        }
    }
}