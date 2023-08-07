package com.example.myprofile.utils

import android.content.ContentProvider
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.ContactsContract
import com.example.myprofile.data.Contact
import java.lang.ref.WeakReference

/**
 * The `ContactsContentProvider` is a content provider that allows retrieving contacts from the phone book.
 * It provides methods to fetch contacts and their information such as avatar, name, organization, and address.
 * This provider is implemented based on the Singleton pattern.
 */
class ContactsContentProvider : ContentProvider() {

    companion object {
        private var instance: ContactsContentProvider? = null

        /**
         * Creates and returns an instance of `ContactsContentProvider`.
         * If the instance does not exist, it creates a new one, and if it exists, it returns the existing instance.
         *
         * @param context the application context needed for fetching contacts.
         * @return the `ContactsContentProvider` instance.
         */
        fun getInstance(context: Context): ContactsContentProvider {
            if (instance == null) {
                instance = ContactsContentProvider()
                instance?.context = WeakReference(context)
            }
            return instance as ContactsContentProvider
        }
    }

    private var context: WeakReference<Context>? = null

    override fun onCreate(): Boolean {
        return true
    }

    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?
    ): Cursor? {
        val contentResolver = context?.get()?.contentResolver ?: return null

        // Get contacts from the phone using ContactsContract
        return contentResolver.query(
            ContactsContract.Contacts.CONTENT_URI,
            null,
            null,
            null,
            null
        )
    }

    override fun getType(uri: Uri): String? {
        return null
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        return null
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {
        return 0
    }

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<out String>?
    ): Int {
        return 0
    }

    /**
     * Retrieves the list of contacts from the phone book.
     *
     * @return the list of `Contact` objects containing contact information.
     */
    fun getPhoneContacts(): List<Contact> {
        val contentResolver = context?.get()?.contentResolver ?: return emptyList()
        val cursor = contentResolver.query(
            ContactsContract.Contacts.CONTENT_URI,
            arrayOf(
                ContactsContract.Contacts._ID,
                ContactsContract.Contacts.DISPLAY_NAME
            ),
            null,
            null,
            ContactsContract.Contacts.DISPLAY_NAME + " ASC"
        )
        return parseCursor(cursor)
    }

    /**
     * Parses the cursor obtained from the contacts database query and returns a list of contacts.
     *
     * @param cursor the cursor containing the result of the contacts database query.
     * @return the list of `Contact` objects containing contact information.
     */
    private fun parseCursor(cursor: Cursor?): List<Contact> {
        val parsedContacts = mutableListOf<Contact>()
        cursor?.use {
            val idColumnIndex = it.getColumnIndex(ContactsContract.Contacts._ID)
            val nameColumnIndex = it.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)

            while (it.moveToNext()) {
                val id = it.getLong(idColumnIndex)
                val avatar = getContactAvatar(id)
                val name = it.getString(nameColumnIndex) ?: ""
                val organization = getContactOrganization(id)
                val address = getContactAddress(id)

                val contact = Contact(avatar, name, organization, address)
                parsedContacts.add(contact)
            }
        }
        return parsedContacts
    }

    /**
     * Retrieves the organization of the contact with the specified identifier.
     *
     * @param contactId the identifier of the contact.
     * @return the organization of the contact or an empty string if the information is not found.
     */
    private fun getContactOrganization(contactId: Long): String {
        val contentResolver = context?.get()?.contentResolver ?: return ""
        val organizationCursor = contentResolver.query(
            ContactsContract.Data.CONTENT_URI,
            arrayOf(ContactsContract.CommonDataKinds.Organization.COMPANY),
            "${ContactsContract.Data.CONTACT_ID} = ? AND ${ContactsContract.Data.MIMETYPE} = ?",
            arrayOf(contactId.toString(), ContactsContract.CommonDataKinds.Organization.CONTENT_ITEM_TYPE),
            null
        )
        val organizationColumnIndex = organizationCursor?.getColumnIndex(ContactsContract.CommonDataKinds.Organization.COMPANY)
        val organization = if (organizationCursor?.moveToFirst() == true && organizationColumnIndex != null) {
            organizationCursor.getString(organizationColumnIndex) ?: ""
        } else {
            ""
        }
        organizationCursor?.close()
        return organization
    }

    /**
     * Retrieves the avatar of the contact with the specified identifier.
     *
     * @param contactId the identifier of the contact.
     * @return a string containing the path to the contact's avatar or an empty string if the information is not found.
     */
    private fun getContactAvatar(contactId: Long): String {
        val contentResolver = context?.get()?.contentResolver ?: return ""

        val avatarCursor = contentResolver.query(
            ContactsContract.Data.CONTENT_URI,
            arrayOf(ContactsContract.CommonDataKinds.Photo.PHOTO_URI),
            "${ContactsContract.Data.CONTACT_ID} = ? AND ${ContactsContract.Data.MIMETYPE} = ?",
            arrayOf(contactId.toString(), ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE),
            null
        )
        val avatarColumnIndex = avatarCursor?.getColumnIndex(ContactsContract.CommonDataKinds.Photo.PHOTO_URI)
        val avatar = if (avatarCursor?.moveToFirst() == true && avatarColumnIndex != null) {
            avatarCursor.getString(avatarColumnIndex) ?: ""
        } else {
            ""
        }
        avatarCursor?.close()
        return avatar
    }

    /**
     * Retrieves the address of the contact with the specified identifier.
     *
     * @param contactId the identifier of the contact.
     * @return the address of the contact or an empty string if the information is not found.
     */
    private fun getContactAddress(contactId: Long): String {
        val contentResolver = context?.get()?.contentResolver ?: return ""

        val addressCursor = contentResolver.query(
            ContactsContract.Data.CONTENT_URI,
            arrayOf(ContactsContract.CommonDataKinds.StructuredPostal.FORMATTED_ADDRESS),
            "${ContactsContract.Data.CONTACT_ID} = ? AND ${ContactsContract.Data.MIMETYPE} = ?",
            arrayOf(contactId.toString(), ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE),
            null
        )
        val addressColumnIndex = addressCursor?.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.FORMATTED_ADDRESS)
        val address = if (addressCursor?.moveToFirst() == true && addressColumnIndex != null) {
            addressCursor.getString(addressColumnIndex) ?: ""
        } else {
            ""
        }
        addressCursor?.close()
        return address
    }
}
