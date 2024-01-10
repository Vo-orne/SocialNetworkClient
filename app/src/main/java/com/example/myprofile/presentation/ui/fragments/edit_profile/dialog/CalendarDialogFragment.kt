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

class CalendarDialogFragment: AppCompatDialogFragment() {
    private lateinit var binding: DialogCalendarBinding

    private var listener: DialogCalendarListener? = null

    fun setListener(listener: DialogCalendarListener) {
        this.listener = listener
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_calendar, null)
        val builder = AlertDialog.Builder(requireContext()).setView(dialogView)
        binding = DialogCalendarBinding.bind(dialogView)
        setListeners()
        return builder.create()
    }

    private fun setListeners() {
        val calendar = Calendar.getInstance()
        var date = "${calendar.get(Calendar.DAY_OF_MONTH)}/${calendar.get(Calendar.MONTH) + 1}/${calendar.get(Calendar.YEAR)}"

        binding.calendarViewCalendar.setOnDateChangeListener { _, year, month, dayOfMonth ->
            date = "$dayOfMonth/${(month + 1)}/$year"
        }
        binding.textViewCalendarSave.setOnClickListener {
            listener?.onDateSelected(date)
            dismiss()
        }

        binding.textViewCalendarCancel.setOnClickListener {
            dismiss()
        }
    }
}