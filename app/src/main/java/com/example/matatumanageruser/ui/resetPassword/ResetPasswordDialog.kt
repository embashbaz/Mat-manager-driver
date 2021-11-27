package com.example.matatumanageradmin.ui.resetPassword

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.example.matatumanageruser.databinding.ResetPasswordBinding
import com.example.matatumanageruser.ui.other.stringFromTl

class ResetPasswordDialog : DialogFragment(){


    lateinit var resetPasswordBinding: ResetPasswordBinding
    internal lateinit var listener: ResetPasswordDialogListener

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {

            val builder = AlertDialog.Builder(it)

            val inflater = requireActivity().layoutInflater;
            resetPasswordBinding = ResetPasswordBinding.inflate(inflater)
            val view =resetPasswordBinding.root
            builder.setView(view)


            resetPasswordBinding.resetPasswordButton.setOnClickListener {
                if(stringFromTl(resetPasswordBinding.emailResetPassword) == stringFromTl(resetPasswordBinding.emailResetPassword)){
                    listener.onSaveButtonClicked(stringFromTl(resetPasswordBinding.emailResetPassword))
                }

                dialog?.dismiss()
            }

            builder.create()

        }?: throw IllegalStateException("Activity cannot be null")
    }

    interface ResetPasswordDialogListener {
        fun onSaveButtonClicked(email: String)
    }

    fun setListener(listener: ResetPasswordDialogListener) {
        this.listener = listener
    }
}