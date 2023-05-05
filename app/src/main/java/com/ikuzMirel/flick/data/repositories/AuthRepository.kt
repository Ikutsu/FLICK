package com.ikuzMirel.flick.data.repositories

import com.ikuzMirel.flick.data.dto.login.request.LoginRequestDto
import com.ikuzMirel.flick.data.dto.signup.request.SignupRequestDto
import com.ikuzMirel.flick.data.utils.ResponseResult
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun login(request: LoginRequestDto): Flow<ResponseResult<String>>
    suspend fun signUp(request: SignupRequestDto): Flow<ResponseResult<String>>
    suspend fun authenticate(): Flow<ResponseResult<String>>
}