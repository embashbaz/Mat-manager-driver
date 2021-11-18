package com.example.matatumanageruser.ui.expenses

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.matatumanageruser.R
import com.example.matatumanageruser.data.Expense
import com.example.matatumanageruser.databinding.FragmentExpenseListBinding
import com.example.matatumanageruser.ui.expenseDetail.ExpenseDetailDialog
import com.example.matatumanageruser.ui.issues.IssuesViewModel
import com.example.matatumanageruser.ui.other.DefaultRecyclerAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ExpenseListFragment : Fragment(), ExpenseDetailDialog.ExpenseDetailDialogListener {

    private lateinit var expenseListBinding: FragmentExpenseListBinding
    private val expenseListViewModel : ExpenseListViewModel by viewModels()
    private lateinit var  defaultRecyclerAdapter: DefaultRecyclerAdapter
    private var driverId = ""


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        expenseListBinding = FragmentExpenseListBinding.inflate(inflater,container, false)
        val view = expenseListBinding.root
        defaultRecyclerAdapter = DefaultRecyclerAdapter { expense -> onExpenseClicked(expense) }

        getExpenses()
        newExpense()

        return view
    }

    private fun newExpense() {
        expenseListViewModel.newExpenseAction.observe(viewLifecycleOwner, {
            openExpenseDetailDialog(false, null)

        })
    }

    private fun getExpenses() {
        expenseListViewModel.getExpenseList(driverId)
        expenseListViewModel.expenseList.observe(viewLifecycleOwner, {
            when(it){
                is ExpenseListViewModel.ExpenseStatus.Success -> {
                    defaultRecyclerAdapter.setData(it.expenses as ArrayList<Any>)
                    setRecyclerView()
                }
            }

        })
    }

    private fun onExpenseClicked(expense: Any) {
        if(expense is Expense){
            expenseListViewModel.setClickedExpenseObject(expense)
            expenseListViewModel.expenseObject.observe(viewLifecycleOwner, {
                if (it != null){
                    openExpenseDetailDialog(true, it)
                    expenseListViewModel.setNextActionNewExpense(false)
                }
            })

        }
    }

    fun setRecyclerView(){
        expenseListBinding.expenseListRecycler.layoutManager = LinearLayoutManager(activity)
        expenseListBinding.expenseListRecycler.adapter = defaultRecyclerAdapter
    }

    private fun openExpenseDetailDialog(type: Boolean, expense: Expense?){
        val expenseDialog = ExpenseDetailDialog(type, expense)
        expenseDialog.setListener(this)
        expenseDialog.show(parentFragmentManager, "Expense")

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.expense_list_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.new_expense_menu){
            expenseListViewModel.setNextActionNewExpense(true)
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onSaveButtonClicked(expense: Expense) {
        expenseListViewModel.createNewExpense(expense)
        expenseListViewModel.addExpenseResult.observe(viewLifecycleOwner, {
            when(it){
                is ExpenseListViewModel.ExpenseStatus.Success-> {

                }
            }

        })
    }

}