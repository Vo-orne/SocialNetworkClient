package com.example.myprofile.presentation.ui.fragments.add_contact

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myprofile.R
import com.example.myprofile.data.model.Contact
import com.example.myprofile.databinding.FragmentAddContactBinding
import com.example.myprofile.domain.states.ApiState
import com.example.myprofile.presentation.ui.base.BaseFragment
import com.example.myprofile.presentation.ui.fragments.add_contact.adapter.AddContactsAdapter
import com.example.myprofile.presentation.ui.fragments.add_contact.adapter.interfaces.AddContactActionListener
import com.example.myprofile.presentation.utils.ext.gone
import com.example.myprofile.presentation.utils.ext.invisible
import com.example.myprofile.presentation.utils.ext.isInternetAvailable
import com.example.myprofile.presentation.utils.ext.log
import com.example.myprofile.presentation.utils.ext.navigateToFragment
import com.example.myprofile.presentation.utils.ext.visible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

/**
 * Fragment for adding contacts.
 */
@AndroidEntryPoint
class AddContactFragment :
    BaseFragment<FragmentAddContactBinding>(FragmentAddContactBinding::inflate) {

    /**
     * ViewModel to manage the data used in this fragment.
     */
    private val viewModel: AddContactViewModel by viewModels()

    /**
     * Progress bar to display the download process.
     */
    private lateinit var progressBar: ProgressBar

    /**
     * Adapter for displaying contacts in the list.
     */
    private val adapter: AddContactsAdapter by lazy {
        AddContactsAdapter(listener = object : AddContactActionListener {

            /**
             * Handles the action of adding a contact.
             *
             * @param contact The contact to be added.
             * @param position The position of the contact in the list.
             */
            override fun onClickAddButton(contact: Contact, position: Int) {
                viewModel.addContact(contact)
            }
        })
    }

    /**
     * Initializes the progressBar, configures the RecyclerView, observes the data,
     * and configures the click handlers.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        progressBar = binding.progressBar
        setRecyclerView()
        setObserves()
        setListeners()
    }

    /**
     * Sets up the RecyclerView with the appropriate layout manager and adapter.
     */
    private fun setRecyclerView() {
        with(binding) {
            if (requireContext().isInternetAvailable()) {
                // If internet is available, hide the "No internet" messages and fetch users.
                textViewAddContactNoInternet.gone()
                textViewAddContactFindInternet.gone()
                viewModel.getAllUsers()
                val layoutManager = LinearLayoutManager(requireContext())
                recyclerViewAddContactUsers.layoutManager = layoutManager
                recyclerViewAddContactUsers.adapter = adapter

            } else {
                // If internet is not available, show the "No internet" messages.
                textViewAddContactNoInternet.visible()
                textViewAddContactFindInternet.visible()
            }
        }
    }

    /**
     * Sets up observers to listen for changes in LiveData from the ViewModel.
     */
    private fun setObserves() {
        lifecycleScope.launch {
            // Observe the list of contacts to add and update the adapter.
            viewModel.contactsToAdd.observe(viewLifecycleOwner, Observer {
                adapter.submitList(it)
            })
        }
        lifecycleScope.launch {
            // Observe the state of all users and update the UI based on the state.
            viewModel.allUsersLiveData.observe(viewLifecycleOwner, Observer { apiState ->
                when (apiState) {
                    is ApiState.Success<*> -> {
                        // Hide progress bar on success.
                        progressBar.invisible()
                    }

                    is ApiState.Error -> {
                        // Hide progress bar and log the error.
                        progressBar.invisible()
                        log(apiState.error)
                    }

                    is ApiState.Initial -> {
                        // Hide progress bar and log the initial state.
                        progressBar.invisible()
                        log(apiState)
                    }

                    is ApiState.Loading -> {
                        // Show progress bar while loading.
                        progressBar.visible()
                        log(apiState)
                    }
                }
            })
        }
        lifecycleScope.launch {
            // Observe the states of contacts and update the adapter with the new states.
            viewModel.states.observe(viewLifecycleOwner, Observer {
                adapter.setStates(it)
            })
        }
    }

    /**
     * Sets up click listeners for the UI elements.
     */
    override fun setListeners() {
        with(binding) {
            // Set up a click listener to navigate back to the previous fragment.
            imageButtonAddContactBack.setOnClickListener {
                navigateToFragment(R.id.action_addContactFragment_to_pagerFragment)
            }
        }
    }
}
