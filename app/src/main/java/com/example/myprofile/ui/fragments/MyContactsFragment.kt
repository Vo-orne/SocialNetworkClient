package com.example.myprofile.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myprofile.R
import com.example.myprofile.data.Contact
import com.example.myprofile.databinding.FragmentMyContactsBinding
import com.example.myprofile.ui.AddContactDialogFragment
import com.example.myprofile.ui.adapters.ContactActionListener
import com.example.myprofile.ui.adapters.ContactsAdapter
import com.example.myprofile.ui.adapters.ViewPagerFragments
import com.example.myprofile.utils.ext.factory
import com.example.myprofile.utils.ext.navigateToFragment
import com.example.myprofile.utils.ext.swipeToDelete
import com.example.myprofile.viewmodel.ContactsViewModel
import com.google.android.material.snackbar.Snackbar

/**
 * Fragment for displaying the list of contacts
 */
class MyContactsFragment : Fragment() { // TODO: BaseFragment?

    private lateinit var binding: FragmentMyContactsBinding
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

            override fun onLongClick(contact: Contact, position: Int) {
                clickInSelectMode(contact, position)
            }
        })
    }

    private fun clickInSelectMode(contact: Contact, position: Int) {
        adapter.toggleSelection(contact, position)
        val selectedItems = adapter.getSelectedItems()
        Log.d("myLog", "selectedItems = $selectedItems")
        binding.imageViewMyContactsDeleteSelectMode?.visibility = View.VISIBLE
        if (selectedItems.isEmpty()) {
            adapter.clearSelection()
            binding.imageViewMyContactsDeleteSelectMode!!.visibility = View.GONE
        }
    }

    /**
     * ViewModel for managing the list of contacts
     */
    private val viewModel: ContactsViewModel by viewModels { factory() }

    /**
     * Method called when the fragment's view is created
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMyContactsBinding.inflate(inflater, container, false)

        // Observer for changes in the list of contacts in ViewModel

        // Set LayoutManager and adapter for the RecyclerView
        val layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewContacts.layoutManager = layoutManager
        binding.recyclerViewContacts.adapter = adapter
        // Enable swipe-to-delete functionality for contacts

        return binding.root
    }

    /**
     * Method called after the fragment's view is created
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.contacts.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
        })
        setListeners()
    }


    /**
     * Private method to set event listeners
     */

    private fun setListeners() {
        binding.recyclerViewContacts.swipeToDelete(
            deleteFunction = { contact, position -> viewModel.deleteUser(contact, position) },
            showSnackbar = { showSnackbar() }
        )
        // Add a click listener for the button to switch to MyProfileFragment
        binding.imageButtonMyContactsBack.setOnClickListener {
            // Get the reference to ViewPager2 from PagerFragment
            (parentFragment as PagerFragment).getViewPager().currentItem =
                ViewPagerFragments.PROFILE_FRAGMENT.position // TODO: constants?

            // Switch to MyProfileFragment by setting the current item of the ViewPager2
            // Assuming MyContactsFragment is at index 1
        }
        binding.textViewMyContactsAddContacts.setOnClickListener {
            AddContactDialogFragment().show(
                childFragmentManager,
                "AddContactDialog"
            ) // TODO: constants?
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
    fun showSnackbar() { // TODO: to ext method
        val snackbar = Snackbar.make(
            binding.root,
            R.string.contact_removed,
            Snackbar.LENGTH_LONG
        )
        snackbar.setAction(R.string.cancel) {
            // Restore the last deleted contact in ViewModel
            viewModel.restoreLastDeletedContact()
        }
        snackbar.show()
        // Automatically close the Snackbar after 5 seconds
    }
}