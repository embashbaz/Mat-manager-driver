package com.example.matatumanageruser.ui.stat

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.matatumanageruser.data.Expense
import com.example.matatumanageruser.data.MainRepository
import com.example.matatumanageruser.data.Statistics
import com.example.matatumanageruser.ui.expenses.ExpenseListViewModel
import com.example.matatumanageruser.utils.DispatcherProvider
import com.example.matatumanageruser.utils.OperationStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StatViewModel @Inject constructor(val repository: MainRepository,
                                        private val dispatcher: DispatcherProvider
): ViewModel(){

    private var _statsValues = MutableLiveData<StatStatus>(StatStatus.Empty)
    val statsValues : LiveData<StatStatus>
            get() = _statsValues


    fun getStat(driverId: String){

        viewModelScope.launch(dispatcher.io){
            _statsValues.postValue(StatStatus.Loading)
            when(val response = repository.getStats("",driverId,"a","a")){
                is OperationStatus.Error -> StatStatus.Failed(response.message!!)
                is OperationStatus.Success -> {
                    if (response.data!!.isEmpty()){
                        _statsValues.postValue( StatStatus.Failed("No data was returned"))
                    }else{
                        _statsValues.postValue(StatStatus.Success("success", response.data))
                    }

                }
            }
        }



    }



    sealed class StatStatus{
        class Success(val resultText: String, val stats: List<Statistics>): StatStatus()
        class Failed(val errorText: String): StatStatus()
        object Loading: StatStatus()
        object Empty: StatStatus()
    }

}