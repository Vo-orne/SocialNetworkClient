package com.example.myprofile.fragments

import android.Manifest.permission.READ_CONTACTS
import android.content.ContentResolver
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myprofile.Constants
import com.example.myprofile.R
import com.example.myprofile.databinding.FragmentMyContactsBinding
import com.example.myprofile.mycontacts.*
import com.example.myprofile.utils.navigateToFragment
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

class MyContactsFragment : Fragment() {
    private var _binding: FragmentMyContactsBinding? = null
    private val binding get() = _binding!!
    private lateinit var contactsViewModel: ContactsViewModel
    private lateinit var recyclerViewContacts: RecyclerView
    private lateinit var contactsAdapter: ContactsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMyContactsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //EventBus.getDefault().register(this)
        contactsViewModel = ViewModelProvider(this)[ContactsViewModel::class.java]

        setupRecyclerView()

        // Configure the watcher to update the user interface when data changes
        contactsViewModel.contactsList.observe(viewLifecycleOwner) { contactsList ->
            contactsAdapter.contacts = contactsList as MutableList<Contact> // Update adapter data
        }

        //requestContactsPermission()

        setListeners()
    }

    private fun setupRecyclerView() {
        recyclerViewContacts = binding.recyclerViewContacts
        recyclerViewContacts.layoutManager = LinearLayoutManager(requireContext())
        contactsAdapter = ContactsAdapter(
            contactsViewModel.contactsList.value?.toMutableList() ?: mutableListOf(),
            binding.fragmentMyContacts, requireContext()
        )
        recyclerViewContacts.adapter = contactsAdapter
        swipeToDelete()
    }

    private fun swipeToDelete() {
        val itemTouchHelperCallback = object :
            ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                // There is no need to implement drag and drop, so we return false
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.bindingAdapterPosition
                val deletedContact = contactsAdapter.getContactAtPosition(position)
                contactsAdapter.deleteContact(position)
                contactsAdapter.showUndoSnackbar(deletedContact, position)
            }
        }
        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(recyclerViewContacts)
    }

    private fun setListeners() {
        binding.buttonMyContactsBack.setOnClickListener {
            navigateToFragment(R.id.action_myContactsFragment_to_myProfileFragment)
        }
        binding.textViewMyContactsAddContacts.setOnClickListener {
            AddContactDialogFragment().show(childFragmentManager, "AddContactDialog")
        }
    }

//    private fun requestContactsPermission() {
//        if (ContextCompat.checkSelfPermission(
//                requireContext(),
//                READ_CONTACTS
//            ) != PackageManager.PERMISSION_GRANTED
//        ) {
//            ActivityCompat.requestPermissions(
//                requireActivity(),
//                arrayOf(READ_CONTACTS),
//                Constants.CONTACTS_PERMISSION_CODE
//            )
//        } else {
//            contactsViewModel.loadContacts(getContactsPhoneBook())
//        }
//    }
//
//    private val requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
//        if (isGranted) {
//            contactsViewModel.loadContacts(getContactsPhoneBook())
//        } else {
//            navigateToFragment(R.id.action_myContactsFragment_to_myProfileFragment)
//        }
//    }


//    private fun getContactsPhoneBook(): MutableList<Contact> {
//        val contentResolver: ContentResolver = requireContext().contentResolver
//        val contactsUri: Uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI
//        val projection: Array<String> = arrayOf(
//            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
//            ContactsContract.CommonDataKinds.Phone.PHOTO_URI
//        )
//
//        val sortOrder = "${ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME} ASC"
//
//        val cursor: Cursor? = contentResolver.query(contactsUri, projection, null, null, sortOrder)
//
//        val result = mutableListOf<Contact>()
//        cursor?.use {
//            val nameColumnIndex: Int =
//                it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)
//            val photoColumnIndex: Int =
//                it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_URI)
//
//            while (it.moveToNext()) {
//                val name: String = it.getString(nameColumnIndex)
//                val photoUriString: String? = it.getString(photoColumnIndex)
//                val photoUri: Uri? = photoUriString?.let { uriString ->
//                    Uri.parse(uriString)
//                }
//
//                val contact = if (photoUri != null) {
//                    Contact(photoUri.toString(), name, "")
//                } else {
//                    Contact("", name, "")
//                }
//                addContact(contact)
//                result.add(contact)
//            }
//        }
//        return result
//    }

//    @Subscribe
//    fun onContactEvent(event: ContactEvent) {
//        val contact = event.contact
//        addContact(contact)
//    }
//
//    private fun addContact(contact: Contact) {
//        contactsAdapter.contacts.add(contact)
//        contactsAdapter.notifyItemInserted(contactsAdapter.contacts.size - 1)
//    }

    override fun onDestroyView() {
        super.onDestroyView()
        EventBus.getDefault().unregister(this)
        _binding = null
    }
}
