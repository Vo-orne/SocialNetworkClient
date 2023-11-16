package com.example.myprofile.presentation.ui.fragments.sign_up_extended

import android.content.Context
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
import com.example.myprofile.presentation.utils.Constants
import com.example.myprofile.presentation.utils.ext.log
import com.example.myprofile.presentation.utils.ext.navigateToFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SignUpExtendedFragment : BaseFragment<FragmentSingUpExtendedBinding>(FragmentSingUpExtendedBinding::inflate) {

    private val viewModel: SignUpExtendedViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListeners()
        setObservers()
    }

    fun setObservers() {
        lifecycleScope.launch {
            viewModel.registerState.flowWithLifecycle(viewLifecycleOwner.lifecycle).collect {
                when (it) {
                    is ApiState.Success<*> -> {
                        log(it.data.toString())
                        navigateToFragment(R.id.action_signUpExtendedFragment_to_pagerFragment)
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

    private val sharedPreferences: SharedPreferences by lazy {
        requireContext().getSharedPreferences(
            Constants.SHARED_PREFERENCES_KEY, Context.MODE_PRIVATE
        )
    }

    override fun setListeners() {
        binding.buttonSignUpExtendedForward.setOnClickListener {
            registerUser()
        }
        binding.buttonSignUpExtendedCancel.setOnClickListener {
            navigateToFragment(R.id.action_signUpExtendedFragment_to_signUpFragment)
        }
    }

    private fun registerUser() {
        val name = binding.textInputEditTextSignUpExtendedName.text.toString()
        viewModel.registerUser(
            sharedPreferences,
            name,
            binding.textInputEditTextSignUpExtendedPhone.text.toString()
        )

//        call.enqueue(object : Callback<UserResponse> {
//            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
//                log("response.isSuccessful == ${response.isSuccessful}")
//                if (response.isSuccessful) {
//                    val userResponse = response.body()
//                    log("userResponse == $userResponse")
//                    if (userResponse != null) {
//                        // Successful registration - get user data and access tokens
//                        viewModel.saveRegistrationData(
//                            name,
//                            userResponse.accessToken,
//                            userResponse.refreshToken,
//                            sharedPreferences.edit()
//                        )
//                        navigateToFragment(R.id.action_signUpExtendedFragment_to_pagerFragment)
//                        // Тепер ви можете використовувати ці дані для подальших дій
//                    }
//                } else {
//                    // Error processing from the server (for example, invalid data in the request)
//                    log("Error body: ${response.errorBody()?.string()}")
//                    log("Response code: ${response.code()}")
//                    // Тут ви можете обробити текст помилки, який повертає сервер
//                }
//            }
//
//            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
//                // Обробка помилки зв'язку
//            }
//        })
    }
}