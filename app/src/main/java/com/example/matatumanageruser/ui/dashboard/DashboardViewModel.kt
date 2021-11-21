package com.example.matatumanageruser.ui.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.matatumanageruser.data.Bus
import com.example.matatumanageruser.data.Expense
import com.example.matatumanageruser.data.MainRepository
import com.example.matatumanageruser.data.Statistics
import com.example.matatumanageruser.ui.other.getDate
import com.example.matatumanageruser.utils.DispatcherProvider
import com.example.matatumanageruser.utils.OperationStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
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

    private var _startDayResult = MutableLiveData<StartDayStatus>(StartDayStatus.Empty)
    val startDayResult: LiveData<StartDayStatus>
        get() = _startDayResult

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

    fun startDayRequest(plate: String, driverId: String) {

        viewModelScope.launch(dispatcher.io) {
            _startDayResult.postValue(StartDayStatus.Loading)
            when (val busResponse = repository.getBus(plate)) {
                is OperationStatus.Error -> {
                    if (busResponse.message!!.lowercase() == "conflict") {
                        _startDayResult.postValue(StartDayStatus.Failed("No such bus"))
                    } else {
                        _startDayResult.postValue(StartDayStatus.Failed(busResponse.message))
                    }
                }
                is OperationStatus.Success -> {
                    if (busResponse.data!!.status.lowercase() == "in service") {
                        _startDayResult.postValue(StartDayStatus.Failed("Bus already in service"))
                    } else {
                        startDay(plate,driverId, busResponse.data!!)
                    }
                }
            }

        }

    }

    suspend fun startDay(plate: String, driverId: String, bus: Bus){
        val stat = Statistics(dayId = getDate(), busPlate = plate, driverId = driverId)
        when(val response = repository.addStat(stat)){
            is OperationStatus.Error -> _startDayResult.postValue(StartDayStatus.Failed(response.message!!))
            is OperationStatus.Success -> {
                updateBus(bus)
               // _startDayResult.postValue(StartDayStatus.Success("Day has started"))
            }
        }
    }

    suspend fun updateBus(bus: Bus){
        bus.status = "in service"
        when(val response = repository.updateBus(bus)){
            is OperationStatus.Error -> _startDayResult.postValue(StartDayStatus.Success("Day was started but bus was not updated"))
            is OperationStatus.Success -> _startDayResult.postValue(StartDayStatus.Success("Day was started"))
        }
    }





    sealed class StartDayStatus{
        class Success(val resultText: String): StartDayStatus()
        class Failed(val errorText: String): StartDayStatus()
        object Loading: StartDayStatus()
        object Empty: StartDayStatus()
    }


}