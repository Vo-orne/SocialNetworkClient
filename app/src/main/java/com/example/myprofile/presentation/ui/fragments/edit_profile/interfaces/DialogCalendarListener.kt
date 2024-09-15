package com.example.myprofile.presentation.ui.fragments.edit_profile.interfaces

/**
 * Interface to listen for date selection events from the CalendarDialogFragment.
 */
interface DialogCalendarListener {
    /**
     * Called when a date is selected in the calendar dialog.
     * @param date The selected date in the format "day/month/year".
     */
    fun onDateSelected(date: String)
}
