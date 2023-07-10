package com.example.myprofile.mycontacts

import android.app.Dialog
import android.content.ContentResolver
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.example.myprofile.databinding.DialogAddContactBinding
import com.example.myprofile.utils.factory

class AddContactDialogFragment : DialogFragment() {

    private var _binding: DialogAddContactBinding? = null
    private val binding get() = _binding!!
    private var imageUri: Uri? = null

    private val viewModel: AddContactViewModel by viewModels { factory() }


    private val getContent =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let { imageUri ->
            this.imageUri = imageUri
            Glide.with(requireContext())
                .load(imageUri)
                .into(binding.imageViewAddContactContactAvatar)
        }
    }

    /**
     * Create dialog fragment.
     */
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = DialogAddContactBinding.inflate(layoutInflater) // Inflate binding of dialog fragment

        return activity?.let {
            val builder = AlertDialog.Builder(it) // Create dialog
            setupButtons()
            builder.setView(binding.root).create() // Set view and create dialog
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    private fun setupButtons() {
        binding.buttonAddContactSave.setOnClickListener {
            saveContact()
        }

        binding.buttonAddContactBack.setOnClickListener {
            dismiss()
        }

        binding.imageViewAddContactAddPhoto.setOnClickListener {
            getContent.launch("image/*")
        }
    }

    private fun saveContact() {
        val id = 0L
        val imagePath = imageUri?.let { getUriAsString(requireContext().contentResolver, it) }
        val contactName = binding.textInputEditTextAddContactUsername.text.toString()
        val contactCareer = binding.textInputEditTextAddContactCareer.text.toString()
        val contact = Contact(id, imagePath.toString(), contactName, contactCareer)

        viewModel.addContact(contact)
        dismiss()
    }


    /*
    This code uses ContentResolver and MediaStore to get the file path from the Uri.
    If the path can be obtained, it can be returned as a URL string.
    If the path fails, the initial Uri string is returned as a fallback value.
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
        return uri.toString() // Return the initial Uri string if the URL string could not be retrieved
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}