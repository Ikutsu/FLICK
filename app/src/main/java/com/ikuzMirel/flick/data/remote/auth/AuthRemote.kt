package com.ikuzMirel.flick.data.remote.auth

import com.ikuzMirel.flick.data.dto.login.request.LoginRequestDto
import com.ikuzMirel.flick.data.dto.login.response.LoginResponseDto
import com.ikuzMirel.flick.data.dto.signup.request.SignupRequestDto
import com.ikuzMirel.flick.data.utils.ResponseResult

interface AuthRemote {

    suspend fun login(
        request: LoginRequestDto
    ): ResponseResult<LoginResponseDto>

    suspend fun signup(
        request: SignupRequestDto
    ): ResponseResult<String>

    suspend fun authenticate(
        token: String
    ): ResponseResult<String>
}