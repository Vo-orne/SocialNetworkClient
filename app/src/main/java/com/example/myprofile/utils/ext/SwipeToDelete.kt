package com.example.myprofile.utils.ext

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.myprofile.data.Contact

fun RecyclerView.swipeToDelete(
    deleteFunction: (contact: Contact, position: Int) -> Unit,
    showSnackbar: () -> Unit
) {
    val itemTouchHelperCallback =
        object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ) = false

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            // Get the position of the contact to delete and the contact itself
            val position = viewHolder.bindingAdapterPosition
            val deletedContact = viewHolder.itemView.tag as Contact
            // Delete the contact from ViewModel and adapter's list
            deleteFunction(deletedContact, position)
            // Show a Snackbar with a message about the contact deletion
            showSnackbar()
        }
    }

    ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(this)
}