package com.example.matatumanageruser.ui.expenses

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.matatumanageruser.R
import com.example.matatumanageruser.data.Expense
import com.example.matatumanageruser.databinding.FragmentExpenseListBinding
import com.example.matatumanageruser.ui.other.DefaultRecyclerAdapter


class ExpenseListFragment : Fragment() {

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

        return view
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
                }
    }

    fun setRecyclerView(){
        expenseListBinding.expenseListRecycler.layoutManager = LinearLayoutManager(activity)
        expenseListBinding.expenseListRecycler.adapter = defaultRecyclerAdapter
    }

}