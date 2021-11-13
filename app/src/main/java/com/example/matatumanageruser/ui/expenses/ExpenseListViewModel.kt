package com.example.matatumanageruser.ui.expenses

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.matatumanageruser.data.Expense
import com.example.matatumanageruser.data.MainRepository
import com.example.matatumanageruser.utils.DispatcherProvider
import com.example.matatumanageruser.utils.OperationStatus
import kotlinx.coroutines.launch
import javax.inject.Inject

class ExpenseListViewModel @Inject constructor(val repository: MainRepository,
                                               private val dispatcher: DispatcherProvider
) : ViewModel() {


    private var _expenseList = MutableLiveData<ExpenseStatus>(ExpenseStatus.Empty)
    val expenseList: LiveData<ExpenseStatus>
         get() = _expenseList

    private var _newExpenseAction = MutableLiveData(false)
    val newExpenseAction : LiveData<Boolean>
        get() = _newExpenseAction

    private var _expenseObject = MutableLiveData<Expense>()
    val expenseObject: LiveData<Expense>
        get() = _expenseObject

    fun setNextActionNewExpense(){
        _newExpenseAction.value = true
    }

    fun setClickedExpenseObject(expense: Expense){
        _expenseObject.value = expense
    }

    fun getExpenseList(id: String){
        viewModelScope.launch(dispatcher.io){
            _expenseList.value = ExpenseStatus.Loading
            when(val response = repository.getExpenses("",id,"","")){
                is OperationStatus.Error -> ExpenseStatus.Failed(response.message!!)
                is OperationStatus.Success -> {
                    if (response.data!!.isEmpty()){
                        ExpenseStatus.Failed("No data was returned")
                    }else{
                        ExpenseStatus.Success("success", response.data)
                    }

                }
            }

        }
    }


    sealed class ExpenseStatus{
        class Success(val resultText: String, val expenses: List<Expense>): ExpenseStatus()
        class Failed(val errorText: String): ExpenseStatus()
        object Loading: ExpenseStatus()
        object Empty: ExpenseStatus()
    }

}