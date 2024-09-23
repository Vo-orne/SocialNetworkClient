package com.example.myprofile.presentation.utils.ext

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.myprofile.data.model.Contact
import com.example.myprofile.presentation.ui.fragments.contacts.adapter.ContactsAdapter

/**
 * Extension function to enable swipe-to-delete functionality in a RecyclerView.
 * @param deleteFunction Function to be called to delete the contact. It takes a [Contact] as a parameter.
 * @param showSnackbar Function to be called to show a Snackbar message after a contact is deleted.
 * @param isEnabled Function to check if swipe-to-delete functionality is enabled.
 */
fun RecyclerView.swipeToDelete(
    deleteFunction: (contact: Contact) -> Unit,
    showSnackbar: () -> Unit,
    isEnabled: () -> Boolean
) {
    val itemTouchHelperCallback =
        object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ) = false // This method is not used in swipe-to-delete functionality

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                // Get the position of the contact to delete and the contact itself
                val position = viewHolder.bindingAdapterPosition
                val contact = (adapter as? ContactsAdapter)?.currentList?.get(position)
                // Check if contact and position are valid before proceeding
                if (contact != null && position != RecyclerView.NO_POSITION) {
                    // Delete the contact from ViewModel and adapter's list
                    deleteFunction(contact)
                    // Show a Snackbar with a message about the contact deletion
                    showSnackbar()
                }
            }

            override fun isItemViewSwipeEnabled(): Boolean = isEnabled()
        }

    ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(this)
}