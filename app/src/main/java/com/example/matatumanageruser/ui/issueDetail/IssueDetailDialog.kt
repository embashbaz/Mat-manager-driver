package com.example.matatumanageruser.ui.issueDetail

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment
import com.example.matatumanageruser.data.Issue
import com.example.matatumanageruser.databinding.IssueDialogBinding
import com.example.matatumanageruser.ui.issues.IssueFragment
import com.example.matatumanageruser.ui.other.getDate
import com.example.matatumanageruser.ui.other.stringFromTl

class IssueDetailDialog(val type: Boolean, var issue: Issue?) : DialogFragment(){

    lateinit var issueDetailBinding : IssueDialogBinding
    internal lateinit var listener: IssueDetailDialogListener

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)

            val inflater = requireActivity().layoutInflater;
            issueDetailBinding = IssueDialogBinding.inflate(inflater)

            val view = issueDetailBinding.root
            builder.setView(view)

            if(type){
                changeViewBehaviour()
                populateView()
            }

            issueDetailBinding.saveIssueBt.setOnClickListener {
                createIssue()
                listener.onSaveButtonClicked(issue!!)
                dialog?.dismiss()

            }



            builder.create()


        }?: throw IllegalStateException("Activity cannot be null")
    }

    private fun changeViewBehaviour() {
        issueDetailBinding.saveIssueBt.visibility = View.INVISIBLE
        issueDetailBinding.commentIssueDialog.isEnabled = false
        issueDetailBinding.plateIssueDialog.isEnabled = false
    }

    @SuppressLint("SetTextI18n")
    private fun populateView() {
        if(issue != null){
            issueDetailBinding.plateIssueDialog.editText!!.setText(issue!!.busPlate)
            issueDetailBinding.commentIssueDialog.editText!!.setText(issue!!.comment)
            issueDetailBinding.moreInfoIssueDetailTxt.setText(
                issue!!.date + "\n"+

                                                            issue!!.reason + "\n"+
                                                            issue!!.status)

        }
    }

    private fun createIssue() {

            issue = Issue(
                getDate(),
                "",
                "Created",
                stringFromTl(issueDetailBinding.plateIssueDialog),
                "",
                issueDetailBinding.issueTypeSpinner.selectedItem.toString(),
                issueDetailBinding.commentIssueDialog.editText!!.text.toString()
            )

        }

    interface IssueDetailDialogListener{
        fun onSaveButtonClicked(issue: Issue)

    }

    fun setListener(listener: IssueDetailDialogListener) {
        this.listener = listener
    }

}