package com.example.myprofile.presentation.utils.ext

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

fun RecyclerView.swipeToDelete(
    deleteFunction: (position: Int) -> Unit,
    showSnackbar: () -> Unit,
    isEnabled: () -> Boolean
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
                // Delete the contact from ViewModel and adapter's list
                deleteFunction(position)
                // Show a Snackbar with a message about the contact deletion
                showSnackbar()
            }

            override fun isItemViewSwipeEnabled(): Boolean = isEnabled()

        }

    ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(this)
}