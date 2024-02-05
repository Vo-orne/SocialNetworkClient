package com.example.myprofile.presentation.ui.fragments.auth.sign_up_extended

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.myprofile.R
import com.example.myprofile.databinding.FragmentSingUpExtendedBinding
import com.example.myprofile.domain.ApiState
import com.example.myprofile.presentation.ui.base.BaseFragment
import com.example.myprofile.presentation.ui.fragments.auth.sign_up.SignUpViewModel
import com.example.myprofile.presentation.utils.ext.invisible
import com.example.myprofile.presentation.utils.ext.log
import com.example.myprofile.presentation.utils.ext.navigateToFragment
import com.example.myprofile.presentation.utils.ext.navigateToFragmentWithoutReturning
import com.example.myprofile.presentation.utils.ext.visible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class SignUpExtendedFragment :
    BaseFragment<FragmentSingUpExtendedBinding>(FragmentSingUpExtendedBinding::inflate) {

    private val getContent =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri?.let {
                lifecycleScope.launch {
                    val imageBitmap = withContext(Dispatchers.IO) {
                        Glide.with(requireContext())
                            .asBitmap()
                            .load(it)
                            .submit()
                            .get()
                    }
                    binding.imageViewSignUpExtendedContactAvatar2.invisible()
                    Glide.with(requireContext())
                        .load(imageBitmap)
                        .into(binding.imageViewSignUpExtendedContactAvatar)
                }
            }
        }

    private val args: SignUpExtendedFragmentArgs by navArgs()

    private val viewModel: SignUpViewModel by viewModels()
    private lateinit var progressBar: ProgressBar

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        progressBar = binding.progressBar
        setListeners()
        setObservers()
    }

    private fun setObservers() {
        lifecycleScope.launch {
            viewModel.registerState.flowWithLifecycle(viewLifecycleOwner.lifecycle).collect {
                when (it) {
                    is ApiState.Success<*> -> {
                        progressBar.invisible()
                        log(it.data.toString())
                        navigateToFragmentWithoutReturning(
                            R.id.action_signUpExtendedFragment_to_pagerFragment,
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
            imageViewSignUpExtendedAddPhoto.setOnClickListener {
                openGallery()
            }
        }
    }

    private fun registerUser() {
        with(viewModel) {
            email.value = args.email
            password.value = args.password
            name.value = binding.textInputEditTextSignUpExtendedName.text.toString()
            phone.value = binding.textInputEditTextSignUpExtendedPhone.text.toString()
            registerUser()
        }
    }

    private fun openGallery() {
        getContent.launch("image/*")
    }
}