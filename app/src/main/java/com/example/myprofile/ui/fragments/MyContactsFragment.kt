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
import com.example.myprofile.ui.ContactActionListener
import com.example.myprofile.ui.ContactsAdapter
import com.example.myprofile.utils.factory
import com.example.myprofile.utils.navigateToFragment
import com.example.myprofile.viewmodel.ContactsViewModel
import com.google.android.material.snackbar.Snackbar

class MyContactsFragment : Fragment() {

    private lateinit var binding: FragmentMyContactsBinding
    private lateinit var adapter: ContactsAdapter

    private val viewModel: ContactsViewModel by viewModels { factory() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMyContactsBinding.inflate(inflater, container, false)
        adapter = ContactsAdapter(object : ContactActionListener {

            override fun onContactDelete(contact: Contact, position: Int) {
                viewModel.deleteUser(contact, position)
                showSnackbar()
            }

            override fun onDetailView(contact: Contact) {
                navigateToFragment(
                    MyContactsFragmentDirections.actionMyContactsFragmentToDetailViewFragment(
                        contact
                    )
                )
            }
        })

        viewModel.contacts.observe(viewLifecycleOwner, Observer {
            adapter.contacts = it
        })

        val layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewContacts.layoutManager = layoutManager
        binding.recyclerViewContacts.adapter = adapter
        swipeToDelete()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListeners()
    }

    private fun setListeners() {
        binding.imageButtonMyContactsBack.setOnClickListener {
            navigateToFragment(R.id.action_myContactsFragment_to_myProfileFragment)
        }
        binding.textViewMyContactsAddContacts.setOnClickListener {
            AddContactDialogFragment().show(childFragmentManager, "AddContactDialog")
        }
    }

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
                val position = viewHolder.bindingAdapterPosition
                val deletedContact = viewHolder.itemView.tag as Contact
                viewModel.deleteUser(deletedContact, position)
                showSnackbar()
            }
        }
        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(binding.recyclerViewContacts)
    }

    fun showSnackbar() {
        val snackbar = Snackbar.make(
            binding.root,
            R.string.contact_removed,
            Snackbar.LENGTH_LONG
        )
        snackbar.setAction(R.string.cancel) {
            viewModel.restoreLastDeletedContact()
        }
        snackbar.show()

        // Automatically close the Snackbar after 5 seconds
        Handler(Looper.getMainLooper()).postDelayed({
            snackbar.dismiss()
        }, 5000)
    }
}