package com.example.matatumanageruser.ui.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.matatumanageruser.data.MainRepository
import com.example.matatumanageruser.utils.DispatcherProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel  @Inject
constructor(private var repository: MainRepository,
            private val dispatcher: DispatcherProvider
): ViewModel() {

    private var _expenseCardClicked = MutableLiveData(false)
    val expenseCardClicked: LiveData<Boolean>
            get() = _expenseCardClicked

    private var _profileCardClicked = MutableLiveData(false)
    val profileCardClicked: LiveData<Boolean>
        get() = _profileCardClicked

    private var _issuesCardClicked = MutableLiveData(false)
    val issuesCardClicked: LiveData<Boolean>
        get() = _issuesCardClicked

    private var _tripCardClicked = MutableLiveData(false)
    val tripCardClicked: LiveData<Boolean>
        get() = _tripCardClicked

    fun expenseCardClicked(action: Boolean){
        _expenseCardClicked.value = action
    }

    fun issueCardClicked(action: Boolean){
        _issuesCardClicked.value = action
    }

    fun tripCardClicked(action: Boolean){
        _tripCardClicked.value = action
    }

    fun profileCardClicked(action: Boolean){
        _profileCardClicked.value = action
    }


}