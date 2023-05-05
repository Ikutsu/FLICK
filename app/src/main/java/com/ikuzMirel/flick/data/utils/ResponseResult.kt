package com.ikuzMirel.flick.data.utils

sealed class ResponseResult<T>(){
    data class Success<T>(val data: T? = null) : ResponseResult<T>()
    data class Error<T>(val errorMessage: T) : ResponseResult<T>()

    companion object{
        fun <T> success(data: T? = null) = Success(data)
        fun <T> error(errorMessage: T) = Error(errorMessage)
    }
}
