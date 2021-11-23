package com.example.matatumanageruser.ui.trip

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.matatumanageruser.data.Expense
import com.example.matatumanageruser.data.MainRepository
import com.example.matatumanageruser.data.Trip
import com.example.matatumanageruser.ui.other.getDate
import com.example.matatumanageruser.ui.stat.StatViewModel
import com.example.matatumanageruser.utils.DispatcherProvider
import com.example.matatumanageruser.utils.OperationStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TripViewModel  @Inject constructor(val repository: MainRepository,
                                         private val dispatcher: DispatcherProvider
) : ViewModel(){

    private var _createNewTrip = MutableLiveData<TripStatus>(TripStatus.Empty)
    val createNewTrip : LiveData<TripStatus>
            get() = _createNewTrip

    private var _updateTrip = MutableLiveData<TripStatus>(TripStatus.Empty)
    val updateTrip: LiveData<TripStatus>
        get() = _updateTrip

    fun setNewTripStatusToEmpty(){
        _createNewTrip.value = TripStatus.Empty
    }

    fun setEndTripStatusToEmpty(){
        _updateTrip.value = TripStatus.Empty
    }


    fun createTrip(pickUpPoint: String, amount: String, busPlate: String, driverId: String){

        val actualAmount = amount.toDoubleOrNull()
        if (actualAmount == null && amount.isNotEmpty()){
            _createNewTrip.postValue(TripStatus.Failed("invalid value passed for fare collected"))
            return
        }

        viewModelScope.launch(dispatcher.io){
            _createNewTrip.postValue(TripStatus.Loading)
            val trip = Trip(getDate(), "", busPlate, driverId, "", pickUpPoint, actualAmount?: 0.0,
                       getDate(), "", "started", "")
            when(val response = repository.addTrip(trip)){
                is OperationStatus.Error -> _createNewTrip.postValue(TripStatus.Failed(response.message!!))
                is OperationStatus.Success -> _createNewTrip.postValue(TripStatus.Success("Trip Started", trip))

            }
        }
    }

    fun updateTrip(trip: Trip){
        viewModelScope.launch(dispatcher.io){
            _updateTrip.postValue(TripStatus.Loading)
            when(val response = repository.updateTrip(trip)){
                is OperationStatus.Error -> _updateTrip.postValue(TripStatus.Failed(response.message!!))
                is OperationStatus.Success -> _updateTrip.postValue(TripStatus.Success("Trip updated", null))

            }
        }
    }




    sealed class TripStatus{
        class Success(val resultText: String, val trip: Trip?): TripStatus()
        class Failed(val errorText: String): TripStatus()
        object Loading: TripStatus()
        object Empty: TripStatus()
    }


}