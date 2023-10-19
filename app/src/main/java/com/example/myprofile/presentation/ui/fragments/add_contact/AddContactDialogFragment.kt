package com.example.myprofile.presentation.ui.fragments.add_contact

import android.app.Dialog
import android.content.ContentResolver
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.example.myprofile.databinding.DialogAddContactBinding
import com.example.myprofile.data.model.Contact
import com.example.myprofile.presentation.utils.utils.ext.factory
import com.example.myprofile.presentation.utils.utils.ext.loadImage

/**
 * Dialog fragment for adding a new contact.
 */
class AddContactDialogFragment : DialogFragment() {

    private var _binding: DialogAddContactBinding? = null
    private val binding get() = _binding!!
    private var imageUri: Uri? = null

    /**
     * ViewModel for handling the addition of new contacts
     */
    private val viewModel: AddContactViewModel by viewModels { factory() }

    /**
     * ActivityResultLauncher to get the content (image) URI
     */
    private val getContent =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let { imageUri ->
                this.imageUri = imageUri
                // Load the selected image into the ImageView using Glide
                binding.imageViewAddContactContactAvatar.loadImage(imageUri)
            }
        }

    /**
     * Create dialog fragment.
     */
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding =
            DialogAddContactBinding.inflate(layoutInflater) // Inflate the binding of the dialog fragment

        return activity?.let {
            val builder = AlertDialog.Builder(it) // Create the dialog builder
            setListeners()
            builder.setView(binding.root).create() // Set the view and create the dialog
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    /**
     * Set up event listeners for buttons and ImageView
     */
    private fun setListeners() {
        // Handle click on the "Save" button
        binding.buttonAddContactSave.setOnClickListener {
            saveContact()
        }

        // Handle click on the "Back" button
        binding.buttonAddContactBack.setOnClickListener {
            dismiss()
        }

        // Handle click on the ImageView to add a photo
        binding.imageViewAddContactAddPhoto.setOnClickListener {
            // Launch the content (image) selection intent
            getContent.launch("image/*")
        }
    }

    /**
     * Method to save the newly added contact
     */
    private fun saveContact() {
        val image = imageUri?.let { getUriAsString(requireContext().contentResolver, it) }
        val name = binding.textInputEditTextAddContactUsername.text.toString()
        val career = binding.textInputEditTextAddContactCareer.text.toString()
        val address = binding.textInputEditTextAddContactAddress.text.toString()

        // Create a new Contact object with the entered data

        // Add the new contact to the ViewModel
        viewModel.addContact(Contact(image.toString(), name, career, address))
        dismiss() // Close the dialog after saving the contact
    }

    /**
     * Method to get the file path from the Uri using ContentResolver and MediaStore.
     * This code uses ContentResolver and MediaStore to get the file path from the Uri.
     * If the path can be obtained, it can be returned as a URL string.
     * If the path fails, the initial Uri string is returned as a fallback value.
     */
    private fun getUriAsString(contentResolver: ContentResolver, uri: Uri): String {
        val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = contentResolver.query(uri, filePathColumn, null, null, null)
        cursor?.use {
            if (it.moveToFirst()) {
                val columnIndex = it.getColumnIndex(filePathColumn[0])
                val filePath = it.getString(columnIndex)
                if (filePath != null) {
                    return filePath
                }
            }
        }
        return uri.toString() // Return the initial Uri string if the file path could not be retrieved
    }

    /**
     * Method called when the view of the dialog fragment is destroyed
     */
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Clean up the binding to avoid memory leaks
    }
}
