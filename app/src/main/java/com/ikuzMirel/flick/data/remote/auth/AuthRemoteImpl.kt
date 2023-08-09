package com.ikuzMirel.flick.data.remote.auth

import com.ikuzMirel.flick.data.constants.ENDPOINT_AUTH
import com.ikuzMirel.flick.data.constants.ENDPOINT_LOGIN
import com.ikuzMirel.flick.data.constants.ENDPOINT_REGISTER
import com.ikuzMirel.flick.data.constants.LOGIN_CONFLICTED
import com.ikuzMirel.flick.data.constants.UNAUTHENTICATED
import com.ikuzMirel.flick.data.constants.USERNAME_CONFLICTED
import com.ikuzMirel.flick.data.model.AuthData
import com.ikuzMirel.flick.data.requests.LoginRequest
import com.ikuzMirel.flick.data.requests.SignupRequest
import com.ikuzMirel.flick.data.response.BasicResponse
import com.ikuzMirel.flick.data.response.processHttpResponse
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import javax.inject.Inject

class AuthRemoteImpl @Inject constructor(
    private val client: HttpClient
) : AuthRemote {
    override suspend fun login(request: LoginRequest): BasicResponse<AuthData> {
        return processHttpResponse(
            request = client.post(ENDPOINT_LOGIN) {
                setBody(request)
            },
            specificError = LOGIN_CONFLICTED
        )
    }

    override suspend fun signup(request: SignupRequest): BasicResponse<String> {
        return processHttpResponse(
            request = client.post(ENDPOINT_REGISTER) {
                setBody(request)
            },
            specificError = USERNAME_CONFLICTED
        )
    }

    override suspend fun authenticate(): BasicResponse<String> {
        return processHttpResponse(
            request = client.get(ENDPOINT_AUTH),
            specificError = UNAUTHENTICATED
        )
    }
}
