package com.example.matatumanageruser.ui.tripList

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.matatumanageruser.data.Expense
import com.example.matatumanageruser.data.MainRepository
import com.example.matatumanageruser.data.Trip
import com.example.matatumanageruser.ui.expenses.ExpenseListViewModel
import com.example.matatumanageruser.utils.DispatcherProvider
import com.example.matatumanageruser.utils.OperationStatus
import kotlinx.coroutines.launch
import javax.inject.Inject

class TripListViewModel @Inject constructor(val repository: MainRepository,
                                            private val dispatcher: DispatcherProvider
) : ViewModel() {

    private var _tripList = MutableLiveData<TripListStatus>(TripListStatus.Empty)
    val tripList: LiveData<TripListStatus>
        get() = _tripList


    private var _tripObject = MutableLiveData<Trip>()
    val tripObject: LiveData<Trip>
        get() = _tripObject

    fun setTripObject(trip: Trip){
        _tripObject.value = trip
    }

    fun getTrips(id: String){

        viewModelScope.launch(dispatcher.io){
            _tripList.value = TripListStatus.Loading
            when(val response = repository.getTrips("",id,"","")){
                is OperationStatus.Error -> _tripList.value = TripListStatus.Failed(response.message!!)
                is OperationStatus.Success -> {
                    if (response.data!!.isEmpty()){
                        _tripList.value =  TripListStatus.Failed("No data was returned")
                    }else{
                        _tripList.value =  TripListStatus.Success("success", response.data)
                    }

                }
            }
        }

    }



    sealed class TripListStatus{
        class Success(val resultText: String, val trips: List<Trip>): TripListStatus()
        class Failed(val errorText: String): TripListStatus()
        object Loading: TripListStatus()
        object Empty: TripListStatus()
    }
}