package com.ikuzMirel.flick.data.remote.auth

import com.ikuzMirel.flick.data.model.AuthData
import com.ikuzMirel.flick.data.requests.LoginRequest
import com.ikuzMirel.flick.data.requests.SignupRequest
import com.ikuzMirel.flick.data.response.BasicResponse

interface AuthRemote {

    suspend fun login(
        request: LoginRequest
    ): BasicResponse<AuthData>

    suspend fun signup(
        request: SignupRequest
    ): BasicResponse<String>

    suspend fun authenticate(): BasicResponse<String>
}