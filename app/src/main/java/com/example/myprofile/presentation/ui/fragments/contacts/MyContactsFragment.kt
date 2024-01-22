package com.example.myprofile.presentation.ui.fragments.contacts

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myprofile.R
import com.example.myprofile.data.model.Contact
import com.example.myprofile.databinding.FragmentMyContactsBinding
import com.example.myprofile.domain.ApiState
import com.example.myprofile.presentation.ui.base.BaseFragment
import com.example.myprofile.presentation.ui.fragments.add_contact.AddContactViewModel
import com.example.myprofile.presentation.ui.fragments.contacts.adapter.ContactsAdapter
import com.example.myprofile.presentation.ui.fragments.contacts.adapter.interfaces.ContactActionListener
import com.example.myprofile.presentation.ui.fragments.pager.PagerFragment
import com.example.myprofile.presentation.ui.fragments.pager.PagerFragmentDirections
import com.example.myprofile.presentation.ui.fragments.pager.adapter.utils.ViewPagerFragments
import com.example.myprofile.presentation.utils.ext.invisible
import com.example.myprofile.presentation.utils.ext.log
import com.example.myprofile.presentation.utils.ext.navigateToFragment
import com.example.myprofile.presentation.utils.ext.showSnackbarWithAction
import com.example.myprofile.presentation.utils.ext.swipeToDelete
import com.example.myprofile.presentation.utils.ext.visible
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

/**
 * Fragment for displaying the list of contacts
 */
@AndroidEntryPoint
class MyContactsFragment :
    BaseFragment<FragmentMyContactsBinding>(FragmentMyContactsBinding::inflate) {

    /**
     * ViewModel for managing the list of contacts
     */
    private val viewModel: ContactsViewModel by viewModels()
    private val viewModelAddContact: AddContactViewModel by viewModels()
    private lateinit var progressBar: ProgressBar

    private val adapter: ContactsAdapter by lazy {
        ContactsAdapter(object : ContactActionListener {

            // Event handler for contact deletion
            override fun onContactDelete(contact: Contact, position: Int) {
                // Delete the contact from ViewModel and adapter's list
//                viewModel.deleteUser(contact, position)
                // Show a Snackbar with a message about the contact deletion
                showSnackbar()
                viewModel.deleteUserContact(contact)
                viewModel.deleteUser(contact, position)
            }

            // Event handler for viewing contact details
            override fun onClick(contact: Contact, position: Int) {
                if (adapter.isSelectMode) {
                    clickInSelectMode(contact, position)
                } else {
                    // Navigate to the "DetailViewFragment" with the contact data
                    navigateToFragment(
                        PagerFragmentDirections.actionPagerFragmentToDetailViewFragment(
                            contact
                        )
                    )
                }
            }

            // Event handler for long click.
            override fun onLongClick(contact: Contact, position: Int) {
                clickInSelectMode(contact, position)
            }
        })
    }

    /**
     * Handles long-clicking on a contact while multiselect mode is enabled.
     * @param contact The contact that was clicked.
     * @param position The position of the contact that was clicked.
     */
    private fun clickInSelectMode(contact: Contact, position: Int) {
        if (viewModel.isMultiselect.value == false) {
            viewModel.setMultiselect()
            binding.imageViewMyContactsDeleteSelectMode?.visible()
        }
        adapter.toggleSelection(contact, position)
        val selectedItems = adapter.getSelectedItems()
        if (selectedItems.isEmpty()) {
            viewModel.setMultiselect()
            binding.imageViewMyContactsDeleteSelectMode!!.visibility = View.GONE
        }
    }

    /**
     * Method called after the fragment's view is created
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        progressBar = binding.progressBar
        setRecyclerView()
        setListeners()
        setObservers()
    }

    private fun setRecyclerView() {
        val layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewContacts.layoutManager = layoutManager
        binding.recyclerViewContacts.adapter = adapter
    }

    private fun setObservers() {
        viewModel.contacts.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
        })
        viewModel.isMultiselect.observe(viewLifecycleOwner, Observer {
            adapter.setMultiselect(it)
        })
        lifecycleScope.launch {
            viewModel.contactsLiveData.observe(viewLifecycleOwner, Observer {apiState ->
                when (apiState) {
                    is ApiState.Success<*> -> {
                        progressBar.invisible()
                    }
                    is ApiState.Error -> {
                        progressBar.invisible()
                        log(apiState.error)
                    }
                    is ApiState.Initial -> {
                        progressBar.invisible()
                        log(apiState)
                    }
                    is ApiState.Loading -> {
                        progressBar.visible()
                        log(apiState)
                    }
                }
            })
        }

    }


    /**
     * Private method to set event listeners
     */
    override fun setListeners() {
//        binding.recyclerViewContacts.swipeToDelete(
//            deleteFunction = { position ->
//                viewModel.deleteUserContact(contact)
//            },
//            showSnackbar = {
//                showSnackbar()
//            },
//            isEnabled = {
//                viewModel.isMultiselect.value == false
//            }
//        )
        // Add a click listener for the button to switch to MyProfileFragment
        binding.imageButtonMyContactsBack.setOnClickListener {
            // Get the reference to ViewPager2 from PagerFragment
            (parentFragment as PagerFragment).getViewPager().currentItem =
                ViewPagerFragments.PROFILE_FRAGMENT.ordinal
            // Switch to MyProfileFragment by setting the current item of the ViewPager2
            // Assuming MyContactsFragment is at index 1
        }
        binding.textViewMyContactsAddContacts.setOnClickListener {
            navigateToFragment(R.id.action_pagerFragment_to_addContactFragment)
        }
        binding.imageViewMyContactsDeleteSelectMode?.setOnClickListener {
            val selectedItems = adapter.getSelectedItems()
//            viewModel.deleteSelectedContacts(selectedItems)
            showSnackbar()
            viewModel.deleteSelectedUserContacts(selectedItems)
            viewModel.setMultiselect()
            binding.imageViewMyContactsDeleteSelectMode!!.visibility = View.GONE
        }
    }


    /**
     * Method to show a Snackbar with a message about the contact deletion
     */
    fun showSnackbar() {
        showSnackbarWithAction(R.string.contact_removed, R.string.cancel) {
            viewModel.restoreLastDeletedContact()
        }
    }
}