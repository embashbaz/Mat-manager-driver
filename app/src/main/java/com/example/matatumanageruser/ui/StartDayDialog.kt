package com.example.matatumanageruser.ui

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.example.matatumanageruser.databinding.IssueDialogBinding
import com.example.matatumanageruser.databinding.StartDayDialogBinding
import com.example.matatumanageruser.ui.issueDetail.IssueDetailDialog
import com.example.matatumanageruser.ui.other.stringFromTl

class StartDayDialog : DialogFragment(){

    private lateinit var startDayDialogBinding: StartDayDialogBinding
    internal lateinit var listener: SaveDayDialogListener


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let{
            val builder = AlertDialog.Builder(it)

            val inflater = requireActivity().layoutInflater;
            startDayDialogBinding = StartDayDialogBinding.inflate(inflater)

            val view = startDayDialogBinding.root
            builder.setView(view)

            startDayDialogBinding.startDayBt.setOnClickListener {
                listener.onSaveButtonClicked(stringFromTl(startDayDialogBinding.plateStartDay).trim())
                dialog?.dismiss()
            }





           builder.create()

        }?: throw IllegalStateException("Activity cannot be null")
    }


    interface SaveDayDialogListener{
        fun onSaveButtonClicked(plate: String)

    }

    fun setListener(listener: SaveDayDialogListener) {
        this.listener = listener
    }

}