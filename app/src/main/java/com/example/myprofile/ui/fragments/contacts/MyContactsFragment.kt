package com.example.myprofile.ui.fragments.contacts

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myprofile.R
import com.example.myprofile.ui.base.BaseFragment
import com.example.myprofile.data.model.Contact
import com.example.myprofile.databinding.FragmentMyContactsBinding
import com.example.myprofile.ui.adapters.ContactActionListener
import com.example.myprofile.ui.adapters.ContactsAdapter
import com.example.myprofile.ui.adapters.ViewPagerFragments
import com.example.myprofile.ui.fragments.AddContactDialogFragment
import com.example.myprofile.ui.fragments.PagerFragment
import com.example.myprofile.ui.fragments.PagerFragmentDirections
import com.example.myprofile.utils.Constants.ADD_CONTACT_DIALOG
import com.example.myprofile.utils.ext.factory
import com.example.myprofile.utils.ext.navigateToFragment
import com.example.myprofile.utils.ext.swipeToDelete
import com.example.myprofile.utils.ext.visible
import com.google.android.material.snackbar.Snackbar

/**
 * Fragment for displaying the list of contacts
 */
class MyContactsFragment :
    BaseFragment<FragmentMyContactsBinding>(FragmentMyContactsBinding::inflate) {

    /**
     * ViewModel for managing the list of contacts
     */
    private val viewModel: ContactsViewModel by viewModels { factory() }

    private val adapter: ContactsAdapter by lazy {
        ContactsAdapter(object : ContactActionListener {

            // Event handler for contact deletion
            override fun onContactDelete(contact: Contact, position: Int) {
                // Delete the contact from ViewModel and adapter's list
                viewModel.deleteUser(contact, position)
                // Show a Snackbar with a message about the contact deletion
                showSnackbar()
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
        adapter.toggleSelection(contact, position)
        val selectedItems = adapter.getSelectedItems()
        binding.imageViewMyContactsDeleteSelectMode?.visible()
        if (selectedItems.isEmpty()) {
            adapter.clearSelection()
            binding.imageViewMyContactsDeleteSelectMode!!.visibility = View.GONE
        }
    }

    /**
     * Method called after the fragment's view is created
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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
        viewModel.isMultiselect.observe(viewLifecycleOwner, Observer { // TODO: set
            // adapter.setMultiselect(it)
        })
    }


    /**
     * Private method to set event listeners
     */
    override fun setListeners() {
        binding.recyclerViewContacts.swipeToDelete(
            deleteFunction = { contact, position -> viewModel.deleteUser(contact, position) },
            showSnackbar = { showSnackbar() },
            isEnabled = viewModel.isMultiselect.value ?: false
        )
        // Add a click listener for the button to switch to MyProfileFragment
        binding.imageButtonMyContactsBack.setOnClickListener {
            // Get the reference to ViewPager2 from PagerFragment
            (parentFragment as PagerFragment).getViewPager().currentItem =
                ViewPagerFragments.PROFILE_FRAGMENT.ordinal
            // Switch to MyProfileFragment by setting the current item of the ViewPager2
            // Assuming MyContactsFragment is at index 1
        }
        binding.textViewMyContactsAddContacts.setOnClickListener {
            AddContactDialogFragment().show(
                childFragmentManager,
                ADD_CONTACT_DIALOG
            )
        }
        binding.imageViewMyContactsDeleteSelectMode?.setOnClickListener {
            val selectedItems = adapter.getSelectedItems()
            viewModel.deleteSelectedContacts(selectedItems)
            showSnackbar()
            adapter.clearSelection()
            binding.imageViewMyContactsDeleteSelectMode!!.visibility = View.GONE
        }
    }


    /**
     * Method to show a Snackbar with a message about the contact deletion
     */
    fun showSnackbar() {
        Snackbar.make(
            binding.root,
            R.string.contact_removed,
            Snackbar.LENGTH_LONG
        ).setAction(R.string.cancel) {
            // Restore the last deleted contact in ViewModel
            viewModel.restoreLastDeletedContact()
        }.show() // Automatically close the Snackbar after 5 seconds
    }
}