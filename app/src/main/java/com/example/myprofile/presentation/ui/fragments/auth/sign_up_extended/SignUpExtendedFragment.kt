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
import com.example.myprofile.domain.states.ApiState
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

/**
 * ViewModel of the SignUpFragment and SignUpFragment classes.
 * Responsible for managing the user registration logic in the application.
 * Uses LiveData and StateFlow to monitor data and registration status.
 */
@AndroidEntryPoint
class SignUpExtendedFragment :
    BaseFragment<FragmentSingUpExtendedBinding>(FragmentSingUpExtendedBinding::inflate) {

    /**
     * Register for activity result to pick an image from the gallery
     */
    private val getContent =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri?.let {
                lifecycleScope.launch {
                    // Load the image bitmap from the URI
                    val imageBitmap = withContext(Dispatchers.IO) {
                        Glide.with(requireContext())
                            .asBitmap()
                            .load(it)
                            .submit()
                            .get()
                    }
                    // Hide placeholder and set the selected image
                    binding.imageViewSignUpExtendedContactAvatar2.invisible()
                    Glide.with(requireContext())
                        .load(imageBitmap)
                        .into(binding.imageViewSignUpExtendedContactAvatar)
                }
            }
        }

    /**
     * Retrieve arguments passed to this fragment
     */
    private val args: SignUpExtendedFragmentArgs by navArgs()

    /**
     * ViewModel to manage the data used in this fragment.
     */
    private val viewModel: SignUpViewModel by viewModels()

    /**
     * Progress bar to display the download process.
     */
    private lateinit var progressBar: ProgressBar

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        progressBar = binding.progressBar
        setListeners()
        setObservers()
    }

    /**
     * Method to set observers for StateFlow.
     * This method updates the UI based on the registration state.
     */
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

    /**
     * Method to set event listeners for the UI components.
     * This includes setting click listeners for buttons and image view.
     */
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

    /**
     * Method to register a user by sending data to the ViewModel.
     */
    private fun registerUser() {
        with(viewModel) {
            email.value = args.email
            password.value = args.password
            name.value = binding.textInputEditTextSignUpExtendedName.text.toString()
            phone.value = binding.textInputEditTextSignUpExtendedPhone.text.toString()
            registerUser()
        }
    }

    /**
     * Method to open the gallery to pick an image.
     */
    private fun openGallery() {
        getContent.launch("image/*")
    }
}
