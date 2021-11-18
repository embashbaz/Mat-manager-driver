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

class IssueDetailDialog(val type: Boolean, var issue: Issue?) : DialogFragment(){

    lateinit var issueDetailBinding : IssueDialogBinding
    internal lateinit var listener: IssueDetailDialogListener

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)

            val inflater = requireActivity().layoutInflater;
            issueDetailBinding = IssueDialogBinding.inflate(inflater)

            val view = issueDetailBinding.root

            if(type){
                changeViewBehaviour()
                populateView()
            }else{
                createIssue()
            }

            issueDetailBinding.saveIssueBt.setOnClickListener {
                listener.onSaveButtonClicked(issue!!)
            }



            builder.create()


        }?: throw IllegalStateException("Activity cannot be null")
    }

    private fun changeViewBehaviour() {
        issueDetailBinding.commentIssueDialog.visibility = View.INVISIBLE
        issueDetailBinding.commentIssueDialog.isEnabled = false
    }

    @SuppressLint("SetTextI18n")
    private fun populateView() {
        if(issue != null){
            issueDetailBinding.commentIssueDialog.editText!!.setText(issue!!.comment)
            issueDetailBinding.moreInfoIssueDetailTxt.setText(
                issue!!.date + "\n"+
                                                             issue!!.busPlate + "\n"+
                                                            issue!!.reason + "\n"+
                                                            issue!!.status)

        }
    }

    private fun createIssue() {
        issueDetailBinding.saveIssueBt.setOnClickListener {
            issue = Issue(
                getDate(),
                "",
                "Created",
                "Az455",
                "AzQBcMHYq1aZ5YzstecUxPuiHKz1",
                issueDetailBinding.issueTypeSpinner.selectedItem.toString(),
                issueDetailBinding.commentIssueDialog.editText!!.text.toString()
            )

        }
        }

    interface IssueDetailDialogListener{
        fun onSaveButtonClicked(issue: Issue)

    }

    fun setListener(listener: IssueDetailDialogListener) {
        this.listener = listener
    }

}