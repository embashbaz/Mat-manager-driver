package com.example.matatumanageruser.ui.issues

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.matatumanageruser.data.Expense
import com.example.matatumanageruser.data.Issue
import com.example.matatumanageruser.data.MainRepository
import com.example.matatumanageruser.ui.expenses.ExpenseListViewModel
import com.example.matatumanageruser.utils.DispatcherProvider
import com.example.matatumanageruser.utils.OperationStatus
import kotlinx.coroutines.launch
import javax.inject.Inject

class IssuesViewModel  @Inject constructor(val repository: MainRepository,
                                           private val dispatcher: DispatcherProvider
) : ViewModel() {

    private var _issueList = MutableLiveData<IssueStatus>( IssueStatus.Empty)
    val issueList: LiveData<IssueStatus>
        get() = _issueList

    private var _newIssueAction = MutableLiveData(false)
    val newIssueAction : LiveData<Boolean>
        get() = _newIssueAction

    private var _issueObject = MutableLiveData<Issue>()
    val issueObject: LiveData<Issue>
        get() = _issueObject

    fun setNextActionNewIssue(){
        _newIssueAction.value = true
    }

    fun setClickedIssueObject(issue: Issue){
        _issueObject.value = issue
    }

    fun getIssues(id: String){
        viewModelScope.launch(dispatcher.io){
            _issueList.value = IssueStatus.Loading
            when(val response = repository.getIssues("",id,"","")){
                is OperationStatus.Error -> _issueList.value = IssueStatus.Failed(response.message!!)
                is OperationStatus.Success -> {
                    if (response.data!!.isEmpty()){
                        _issueList.value = IssueStatus.Failed("No data was returned")
                    }else{
                        _issueList.value = IssueStatus.Success("success", response.data)
                    }

                }
            }

        }

    }



    sealed class IssueStatus{
        class Success(val resultText: String, val issues: List<Issue>): IssueStatus()
        class Failed(val errorText: String): IssueStatus()
        object Loading: IssueStatus()
        object Empty: IssueStatus()
    }
}