package com.ikuzMirel.flick.data.auth

interface AuthRepository {
    suspend fun signIn(username: String, password: String): AuthResult<Unit>
    suspend fun signUp(username: String, password: String, email: String): AuthResult<Unit>
    suspend fun authenticate(): AuthResult<Unit>
}