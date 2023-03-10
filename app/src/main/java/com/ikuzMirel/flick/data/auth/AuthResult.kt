package com.ikuzMirel.flick.data.auth

sealed class AuthResult<T>(val data: T? = null){
    class Authenticated<T>(data: T? = null) : AuthResult<T>(data)
    class Unauthenticated<T> : AuthResult<T>()
    class UsernameConflicted<T>: AuthResult<T>()
    class LoginConflicted<T>: AuthResult<T>()
    class NoConnectionError<T>: AuthResult<T>()
    class JsonEncodingException<T>: AuthResult<T>()
    class SocketTimeoutException<T>: AuthResult<T>()
    class Error<T>: AuthResult<T>()
}
