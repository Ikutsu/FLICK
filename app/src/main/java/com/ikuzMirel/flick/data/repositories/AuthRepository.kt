package com.ikuzMirel.flick.data.repositories

import com.ikuzMirel.flick.data.requests.LoginRequest
import com.ikuzMirel.flick.data.requests.SignupRequest
import com.ikuzMirel.flick.data.response.BasicResponse
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun login(request: LoginRequest): Flow<BasicResponse<String>>
    suspend fun signUp(request: SignupRequest): Flow<BasicResponse<String>>
    suspend fun authenticate(): Flow<BasicResponse<String>>
}