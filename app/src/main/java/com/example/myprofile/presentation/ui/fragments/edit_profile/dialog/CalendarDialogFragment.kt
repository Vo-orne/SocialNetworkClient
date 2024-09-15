package com.example.myprofile.presentation.ui.fragments.edit_profile.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatDialogFragment
import com.example.myprofile.R
import com.example.myprofile.databinding.DialogCalendarBinding
import com.example.myprofile.presentation.ui.fragments.edit_profile.interfaces.DialogCalendarListener
import java.util.Calendar

/**
 * This fragment is responsible for displaying a calendar dialog that allows users to select a date.
 */
class CalendarDialogFragment: AppCompatDialogFragment() {

    /**
     * An object to bind to the dialog layout.
     */
    private lateinit var binding: DialogCalendarBinding

    /**
     * Interface for receiving date picker events.
     * Used to pass the selected date back to the fragment or activity.
     */
    private var listener: DialogCalendarListener? = null

    /**
     * Sets the listener that will receive date selection events.
     */
    fun setListener(listener: DialogCalendarListener) {
        this.listener = listener
    }

    /**
     * Creates and returns a dialog box.
     * Uses AlertDialog.Builder to create a dialog with a calendar view.
     */
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_calendar, null)
        val builder = AlertDialog.Builder(requireContext()).setView(dialogView)
        binding = DialogCalendarBinding.bind(dialogView)
        setListeners()
        return builder.create()
    }

    /**
     * Configures listeners for date selection and dialog buttons.
     */
    private fun setListeners() {
        val calendar = Calendar.getInstance()
        var date = "${calendar.get(Calendar.DAY_OF_MONTH)}/${calendar.get(Calendar.MONTH) + 1}/${calendar.get(Calendar.YEAR)}"

        // Sets the listener to the calendar view
        // to update the date variable when a date is selected.
        binding.calendarViewCalendar.setOnDateChangeListener { _, year, month, dayOfMonth ->
            date = "$dayOfMonth/${(month + 1)}/$year"
        }
        // Configures the "Save" button
        // to pass the selected date through the listener and close the dialog.
        binding.textViewCalendarSave.setOnClickListener {
            listener?.onDateSelected(date)
            dismiss()
        }
        // Configures the "Cancel" button to close the dialog without saving changes.
        binding.textViewCalendarCancel.setOnClickListener {
            dismiss()
        }
    }
}