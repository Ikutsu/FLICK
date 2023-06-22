package com.ikuzMirel.flick.data.response

sealed class BasicResponse<T> {
    class Success<T>(val data: T? = null) : BasicResponse<T>()
    class Error<T>(val errorMessage: String) : BasicResponse<T>()
}
