package com.example.matatumanageruser.ui.expenseDetail

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment
import com.example.matatumanageruser.data.Expense
import com.example.matatumanageruser.databinding.ExpenseDialogBinding
import com.example.matatumanageruser.ui.other.stringFromTl

class ExpenseDetailDialog (val type: Boolean, var expense: Expense?) : DialogFragment(){

        lateinit var expenseDetailBiding: ExpenseDialogBinding
        internal lateinit var listener : ExpenseDetailDialogListener


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {

            val builder = AlertDialog.Builder(it)

            val inflater = requireActivity().layoutInflater;
            expenseDetailBiding = ExpenseDialogBinding.inflate(inflater)

            if(type){
                changeViewBehaviour()
                populateView()
            }else{
                createExpense()
            }

            builder.create()

        }?: throw IllegalStateException("Activity cannot be null")
    }

    private fun createExpense() {
        expense = Expense("", "", stringFromTl(expenseDetailBiding.plateExpenseDialog),
                        "", stringFromTl( expenseDetailBiding.amountExpenseDialog).toDouble(),
                        stringFromTl(expenseDetailBiding.reasonExpenseDialog),
                        stringFromTl(expenseDetailBiding.commentExpenseDialog))
    }

    private fun populateView() {
        expenseDetailBiding.amountExpenseDialog.editText?.setText(expense!!.amount.toString())
        expenseDetailBiding.commentExpenseDialog.editText?.setText(expense!!.comment)
        expenseDetailBiding.plateExpenseDialog.editText?.setText(expense!!.busPlate)
        expenseDetailBiding.reasonExpenseDialog.editText?.setText(expense!!.reason)
        expenseDetailBiding.expenseMoreDataDetailTxt.setText(expense!!.date)

    }

    private fun changeViewBehaviour() {
        expenseDetailBiding.amountExpenseDialog.isEnabled = false
        expenseDetailBiding.commentExpenseDialog.isEnabled = false
        expenseDetailBiding.plateExpenseDialog.isEnabled = false
        expenseDetailBiding.reasonExpenseDialog.isEnabled = false
        expenseDetailBiding.saveExpenseBt.visibility = View.INVISIBLE
    }

    interface ExpenseDetailDialogListener {
        fun onSaveButtonClicked(expense: Expense)
    }

    fun setListener(listener: ExpenseDetailDialogListener) {
        this.listener = listener
    }

}