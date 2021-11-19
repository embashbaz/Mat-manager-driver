package com.example.matatumanageruser.ui.expenses

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.matatumanageruser.data.Expense
import com.example.matatumanageruser.data.Issue
import com.example.matatumanageruser.data.MainRepository
import com.example.matatumanageruser.ui.issues.IssuesViewModel
import com.example.matatumanageruser.utils.DispatcherProvider
import com.example.matatumanageruser.utils.OperationStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
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

    private var _addExpenseResult = MutableLiveData<ExpenseStatus>(ExpenseStatus.Empty)
    val addExpenseResult: LiveData<ExpenseStatus>
        get() = _addExpenseResult

    fun setNextActionNewExpense(bool: Boolean){
        _newExpenseAction.value = bool
    }

    fun setClickedExpenseObject(expense: Expense){
        _expenseObject.value = expense
    }

    fun getExpenseList(id: String){
        viewModelScope.launch(dispatcher.io){
            _expenseList.postValue(ExpenseStatus.Loading)
            when(val response = repository.getExpenses("",id,"a","a")){
                is OperationStatus.Error -> ExpenseStatus.Failed(response.message!!)
                is OperationStatus.Success -> {
                    if (response.data!!.isEmpty()){
                        _expenseList.postValue( ExpenseStatus.Failed("No data was returned"))
                    }else{
                        _expenseList.postValue(ExpenseStatus.Success("success", response.data))
                    }

                }
            }
        }
    }

    fun createNewExpense(expense: Expense){
        if (expense.reason.isNotEmpty() && expense.amount > 0.0 ){
            viewModelScope.launch(dispatcher.io) {
                _addExpenseResult.postValue(ExpenseStatus.Loading)
                when(val response = repository.addExpense(expense)){
                    is OperationStatus.Error -> _addExpenseResult.postValue(ExpenseStatus.Failed(response.message!!))
                    is OperationStatus.Success -> _addExpenseResult.postValue(ExpenseStatus.Success(response.message!!, emptyList()))
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