package com.example.matatumanageruser.utils

sealed class OperationStatus<T>( val data: T?, val message: String?){
    class Success<T>(data: T): OperationStatus<T>(data, "success")
    class Error<T>(message: String): OperationStatus<T>(null, message)
}
