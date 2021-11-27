package com.example.matatumanageruser.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.matatumanageruser.data.Driver
import com.example.matatumanageruser.data.MainRepository
import com.example.matatumanageruser.utils.DispatcherProvider
import com.example.matatumanageruser.utils.OperationStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class LoginViewModel @Inject constructor(val repository: MainRepository,
                                         private val dispatcher: DispatcherProvider
) : ViewModel() {


    private var _loginStatus = MutableLiveData<LoginStatus>(LoginStatus.Empty)
    val loginStatus: LiveData<LoginStatus>
        get() = _loginStatus

    fun setLoginStatusToEmpty(){
        _loginStatus.postValue(LoginStatus.Empty)
    }

    fun loginMethod(email: String, password: String){
        if (email.isNotEmpty() && password.isNotEmpty())
        viewModelScope.launch(dispatcher.io) {
            _loginStatus.postValue(LoginStatus.Loading)
            when (val response = repository.login(email, password)){
                is OperationStatus.Error -> _loginStatus.postValue(LoginStatus.Failed(response.message!!))
                is OperationStatus.Success -> _loginStatus.postValue( LoginStatus.Success("Success", response.data))
            }

        }else{
            _loginStatus.value = LoginStatus.Failed("Please give both the password and email")
        }

    }

    sealed class LoginStatus {
        class Success(val resultText: String, val driver: Driver?) : LoginStatus()
        class Failed(val errorText: String) : LoginStatus()
        object Loading : LoginStatus()
        object Empty : LoginStatus()
    }
}
