package com.example.matatumanageruser.ui.issues

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.matatumanageruser.data.Expense
import com.example.matatumanageruser.data.Issue
import com.example.matatumanageruser.data.MainRepository
import com.example.matatumanageruser.ui.expenses.ExpenseListViewModel
import com.example.matatumanageruser.ui.other.getEndOfToday
import com.example.matatumanageruser.ui.other.getFirstDayOfTheMonth
import com.example.matatumanageruser.utils.DispatcherProvider
import com.example.matatumanageruser.utils.OperationStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class IssuesViewModel @Inject constructor(
    val repository: MainRepository,
    private val dispatcher: DispatcherProvider
) : ViewModel() {

    private var _issueList = MutableLiveData<IssueStatus>(IssueStatus.Empty)
    val issueList: LiveData<IssueStatus>
        get() = _issueList

    private var _newIssueAction = MutableLiveData(false)
    val newIssueAction: LiveData<Boolean>
        get() = _newIssueAction

    private var _issueObject = MutableLiveData<Issue>()
    val issueObject: LiveData<Issue>
        get() = _issueObject

    private var _addIssueResult = MutableLiveData<IssueStatus>(IssueStatus.Empty)
    val addIssueResult: LiveData<IssueStatus>
        get() = _addIssueResult

    fun setNextActionNewIssue(bool: Boolean) {
        _newIssueAction.value = bool
    }

    fun setClickedIssueObject(issue: Issue) {
        _issueObject.value = issue
    }

    fun getIssues(id: String) {
        viewModelScope.launch(dispatcher.io) {
            _issueList.postValue(IssueStatus.Loading)
            when (val response = repository.getIssues(
                "", id,
                getFirstDayOfTheMonth(), getEndOfToday()
            )) {
                is OperationStatus.Error -> _issueList.postValue(IssueStatus.Failed(response.message!!))
                is OperationStatus.Success -> {
                    if (response.data!!.isEmpty()) {
                        _issueList.postValue(IssueStatus.Failed("No data was returned"))
                    } else {
                        _issueList.postValue(IssueStatus.Success("success", response.data))
                    }

                }
            }

        }

    }

    fun createNewIssue(issue: Issue) {
        if (issue.reason.isNotEmpty() && issue.driverId.isNotEmpty()) {
            viewModelScope.launch(dispatcher.io) {
                _addIssueResult.postValue(IssueStatus.Loading)
                when (val response = repository.addIssue(issue)) {
                    is OperationStatus.Error -> _addIssueResult.postValue(
                        IssueStatus.Failed(
                            response.message!!
                        )
                    )
                    is OperationStatus.Success -> _addIssueResult.postValue(
                        IssueStatus.Success(
                            response.message!!,
                            emptyList()
                        )
                    )
                }


            }
        }
    }


    sealed class IssueStatus {
        class Success(val resultText: String, val issues: List<Issue>) : IssueStatus()
        class Failed(val errorText: String) : IssueStatus()
        object Loading : IssueStatus()
        object Empty : IssueStatus()
    }
}