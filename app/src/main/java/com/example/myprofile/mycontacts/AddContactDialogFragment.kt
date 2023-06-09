package com.example.myprofile.mycontacts

import android.content.ContentResolver
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.DialogFragment
import com.bumptech.glide.Glide
import com.example.myprofile.databinding.DialogAddContactBinding
import org.greenrobot.eventbus.EventBus

class AddContactDialogFragment : DialogFragment() {

    private var _binding: DialogAddContactBinding? = null
    private val binding get() = _binding!!
    private var imageUri: Uri? = null


    private val getContent = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let { imageUri ->
            this.imageUri = imageUri
            Glide.with(requireContext())
                .load(imageUri)
                .into(binding.imageViewAddContactContactAvatar)
        }
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = DialogAddContactBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupButtons()
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
        val contactName = binding.textInputEditTextAddContactUsername.text.toString()
        val contactCareer = binding.textInputEditTextAddContactCareer.text.toString()
        val imagePath = imageUri?.let { getUriAsString(requireContext().contentResolver, it) }
        val contact = Contact(imagePath.toString(), contactName, contactCareer)

        // Опублікувати подію з контактом
        EventBus.getDefault().post(ContactEvent(contact))

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
        // Return the initial Uri string if the URL string could not be retrieved
        return uri.toString()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}