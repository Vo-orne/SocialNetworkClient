package com.example.myprofile.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myprofile.R
import com.example.myprofile.databinding.FragmentMyContactsBinding
import com.example.myprofile.mycontacts.*
import com.example.myprofile.utils.factory
import com.example.myprofile.utils.navigateToFragment

class MyContactsFragment : Fragment() {

    private lateinit var binding: FragmentMyContactsBinding
    private lateinit var adapter: ContactsAdapter

    private val viewModel: ContactsViewModel by viewModels { factory() }

//    private var _binding: FragmentMyContactsBinding? = null
//    private val binding get() = _binding!!
//    private lateinit var contactsViewModel: ContactsViewModel
//    private lateinit var recyclerViewContacts: RecyclerView
//    private lateinit var contactsAdapter: ContactsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMyContactsBinding.inflate(inflater, container, false)
        adapter = ContactsAdapter(object: ContactActionListener {

            override fun onContactDelete(contact: Contact) {
                viewModel.deleteUser(contact)
            }

            override fun onDetailView(contact: Contact) {
                TODO("Not yet implemented")
            }
        })

        viewModel.contacts.observe(viewLifecycleOwner, Observer {
            adapter.contacts = it
        })

        val layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewContacts.layoutManager = layoutManager
        binding.recyclerViewContacts.adapter = adapter
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
//
//    private fun setupRecyclerView() {
//        recyclerViewContacts = binding.recyclerViewContacts
//        recyclerViewContacts.layoutManager = LinearLayoutManager(requireContext())
//        contactsAdapter = ContactsAdapter(
//            contactsViewModel.contacts.value?.toMutableList() ?: mutableListOf(),
//            binding.fragmentMyContacts, requireContext()
//        )
//        recyclerViewContacts.adapter = contactsAdapter
//        swipeToDelete()
//    }
//
//    private fun swipeToDelete() {
//        val itemTouchHelperCallback = object :
//            ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
//            override fun onMove(
//                recyclerView: RecyclerView,
//                viewHolder: RecyclerView.ViewHolder,
//                target: RecyclerView.ViewHolder
//            ): Boolean {
//                // There is no need to implement drag and drop, so we return false
//                return false
//            }
//
//            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
//                val position = viewHolder.bindingAdapterPosition
//                val deletedContact = contactsAdapter.getContactAtPosition(position)
//                contactsAdapter.deleteContact(position)
////                contactsAdapter.showUndoSnackbar(deletedContact, position)
//            }
//        }
//        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
//        itemTouchHelper.attachToRecyclerView(recyclerViewContacts)
//    }
//
//    private fun setListeners() {
//        binding.imageButtonMyContactsBack.setOnClickListener {
//            navigateToFragment(R.id.action_myContactsFragment_to_myProfileFragment)
//        }
//        binding.textViewMyContactsAddContacts.setOnClickListener {
//            AddContactDialogFragment().show(childFragmentManager, "AddContactDialog")
//        }
//    }
//
////    private fun requestContactsPermission() {
////        if (ContextCompat.checkSelfPermission(
////                requireContext(),
////                READ_CONTACTS
////            ) != PackageManager.PERMISSION_GRANTED
////        ) {
////            ActivityCompat.requestPermissions(
////                requireActivity(),
////                arrayOf(READ_CONTACTS),
////                Constants.CONTACTS_PERMISSION_CODE
////            )
////        } else {
////            contactsViewModel.loadContacts(getContactsPhoneBook())
////        }
////    }
////
////    private val requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
////        if (isGranted) {
////            contactsViewModel.loadContacts(getContactsPhoneBook())
////        } else {
////            navigateToFragment(R.id.action_myContactsFragment_to_myProfileFragment)
////        }
////    }
//
//
////    private fun getContactsPhoneBook(): MutableList<Contact> {
////        val contentResolver: ContentResolver = requireContext().contentResolver
////        val contactsUri: Uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI
////        val projection: Array<String> = arrayOf(
////            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
////            ContactsContract.CommonDataKinds.Phone.PHOTO_URI
////        )
////
////        val sortOrder = "${ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME} ASC"
////
////        val cursor: Cursor? = contentResolver.query(contactsUri, projection, null, null, sortOrder)
////
////        val result = mutableListOf<Contact>()
////        cursor?.use {
////            val nameColumnIndex: Int =
////                it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)
////            val photoColumnIndex: Int =
////                it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_URI)
////
////            while (it.moveToNext()) {
////                val name: String = it.getString(nameColumnIndex)
////                val photoUriString: String? = it.getString(photoColumnIndex)
////                val photoUri: Uri? = photoUriString?.let { uriString ->
////                    Uri.parse(uriString)
////                }
////
////                val contact = if (photoUri != null) {
////                    Contact(photoUri.toString(), name, "")
////                } else {
////                    Contact("", name, "")
////                }
////                addContact(contact)
////                result.add(contact)
////            }
////        }
////        return result
////    }
//
////    @Subscribe
////    fun onContactEvent(event: ContactEvent) {
////        val contact = event.contact
////        addContact(contact)
////    }
////
////    private fun addContact(contact: Contact) {
////        contactsAdapter.contacts.add(contact)
////        contactsAdapter.notifyItemInserted(contactsAdapter.contacts.size - 1)
////    }
}
