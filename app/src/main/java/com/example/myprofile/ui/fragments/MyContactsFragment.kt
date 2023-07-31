package com.example.myprofile.ui.fragments

import androidx.recyclerview.widget.ItemTouchHelper
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myprofile.R
import com.example.myprofile.data.Contact
import com.example.myprofile.databinding.FragmentMyContactsBinding
import com.example.myprofile.ui.AddContactDialogFragment
import com.example.myprofile.ui.adapters.ContactActionListener
import com.example.myprofile.ui.adapters.ContactsAdapter
import com.example.myprofile.utils.ext.factory
import com.example.myprofile.utils.ext.navigateToFragment
import com.example.myprofile.viewmodel.ContactsViewModel
import com.google.android.material.snackbar.Snackbar

/**
 * Fragment for displaying the list of contacts
 */
class MyContactsFragment : Fragment() {

    private lateinit var binding: FragmentMyContactsBinding
    private lateinit var adapter: ContactsAdapter

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
        adapter = ContactsAdapter(object : ContactActionListener {

            // Event handler for contact deletion
            override fun onContactDelete(contact: Contact, position: Int) {
                // Delete the contact from ViewModel and adapter's list
                viewModel.deleteUser(contact, position)
                // Show a Snackbar with a message about the contact deletion
                showSnackbar()
            }

            // Event handler for viewing contact details
            override fun onDetailView(contact: Contact) {
                // Navigate to the "DetailViewFragment" with the contact data
                navigateToFragment(PagerFragmentDirections.actionPagerFragmentToDetailViewFragment(contact))
            }
        })

        // Observer for changes in the list of contacts in ViewModel
        viewModel.contacts.observe(viewLifecycleOwner, Observer {
            adapter.contacts = it
        })

        // Set LayoutManager and adapter for the RecyclerView
        val layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewContacts.layoutManager = layoutManager
        binding.recyclerViewContacts.adapter = adapter
        // Enable swipe-to-delete functionality for contacts
        swipeToDelete()
        return binding.root
    }

    /**
     * Method called after the fragment's view is created
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListeners()
    }

    /**
     * Private method to set event listeners
     */
    private fun setListeners() {
        binding.imageButtonMyContactsBack.setOnClickListener {
            navigateToFragment(R.id.action_myContactsFragment_to_myProfileFragment)
        }
        binding.textViewMyContactsAddContacts.setOnClickListener {
            AddContactDialogFragment().show(childFragmentManager, "AddContactDialog")
        }
    }

    /**
     * Private method to enable swipe-to-delete functionality for contacts
     */
    private fun swipeToDelete() {
        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(
            0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean { // There is no need to implement drag and drop, so we return false
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                // Get the position of the contact to delete and the contact itself
                val position = viewHolder.bindingAdapterPosition
                val deletedContact = viewHolder.itemView.tag as Contact
                // Delete the contact from ViewModel and adapter's list
                viewModel.deleteUser(deletedContact, position)
                // Show a Snackbar with a message about the contact deletion
                showSnackbar()
            }
        }
        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(binding.recyclerViewContacts)
    }

    /**
     * Method to show a Snackbar with a message about the contact deletion
     */
    fun showSnackbar() {
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
        Handler(Looper.getMainLooper()).postDelayed({
            snackbar.dismiss()
        }, 5000)
    }
}