package com.example.matatumanageruser.ui.driverDetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.matatumanageruser.data.Driver
import com.example.matatumanageruser.data.MainRepository
import com.example.matatumanageruser.utils.DispatcherProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DriverDetailViewModel @Inject constructor(val repository: MainRepository,
                                                private val dispatcher: DispatcherProvider
) : ViewModel() {

    private var _driverObject = MutableLiveData<Driver>()
    val driverObject: LiveData<Driver>
            get() = _driverObject

    fun setDriver(driver: Driver){
        _driverObject.value = driver
    }


}