package com.ikuzMirel.flick.data.remote.auth

import android.util.Log
import com.ikuzMirel.flick.data.constants.ENDPOINT_AUTH
import com.ikuzMirel.flick.data.constants.ENDPOINT_LOGIN
import com.ikuzMirel.flick.data.constants.ENDPOINT_REGISTER
import com.ikuzMirel.flick.data.constants.LOGIN_CONFLICTED
import com.ikuzMirel.flick.data.constants.NO_INTERNET_CONNECTION_ERROR
import com.ikuzMirel.flick.data.constants.SOCKET_TIMEOUT_ERROR
import com.ikuzMirel.flick.data.constants.UNAUTHENTICATED
import com.ikuzMirel.flick.data.constants.UNKNOWN_ERROR
import com.ikuzMirel.flick.data.constants.UNKNOWN_HTTP_ERROR
import com.ikuzMirel.flick.data.constants.USERNAME_CONFLICTED
import com.ikuzMirel.flick.data.model.AuthData
import com.ikuzMirel.flick.data.requests.LoginRequest
import com.ikuzMirel.flick.data.requests.SignupRequest
import com.ikuzMirel.flick.data.response.BasicResponse
import com.ikuzMirel.flick.utils.EXCEPTION_MESSAGE
import com.ikuzMirel.flick.utils.NET_EXCEPTION_MESSAGE
import com.ikuzMirel.flick.utils.TAG
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import java.net.ConnectException
import java.net.SocketTimeoutException
import javax.inject.Inject

class AuthRemoteImpl @Inject constructor(
    private val client: HttpClient
) : AuthRemote {
    override suspend fun login(request: LoginRequest): BasicResponse<AuthData> {
        return try {
            val response = client.post(ENDPOINT_LOGIN) {
                setBody(request)
            }   
            when (response.status.value) {
                200 -> {
                    BasicResponse.Success(response.body<AuthData>())
                }
                401 -> {
                    BasicResponse.Error(UNAUTHENTICATED)
                }
                409 -> {
                    BasicResponse.Error(LOGIN_CONFLICTED)
                }
                else -> {
                    Log.e(TAG, NET_EXCEPTION_MESSAGE + response.status.value + response.status.description)
                    BasicResponse.Error(UNKNOWN_HTTP_ERROR)
                }
            }
        } catch (e: ConnectException) {
            Log.e(TAG, EXCEPTION_MESSAGE + e.stackTraceToString())
            BasicResponse.Error(NO_INTERNET_CONNECTION_ERROR)
        } catch (e: SocketTimeoutException) {
            Log.e(TAG, EXCEPTION_MESSAGE + e.stackTraceToString())
            BasicResponse.Error(SOCKET_TIMEOUT_ERROR)
        } catch (e: Exception) {
            Log.e(TAG, EXCEPTION_MESSAGE + e.stackTraceToString())
            BasicResponse.Error(UNKNOWN_ERROR)
        }
    }

    override suspend fun signup(request: SignupRequest): BasicResponse<String> {
        return try {
            val response = client.post(ENDPOINT_REGISTER) {
                setBody(request)
            }
            when (response.status.value) {
                200 -> {
                    BasicResponse.Success()
                }
                401 -> {
                    BasicResponse.Error(UNAUTHENTICATED)
                }
                409 -> {
                    BasicResponse.Error(USERNAME_CONFLICTED)
                }
                else -> {
                    Log.e(TAG, NET_EXCEPTION_MESSAGE + response.status.value + response.status.description)
                    BasicResponse.Error(UNKNOWN_HTTP_ERROR)
                }
            }
        } catch (e: ConnectException) {
            Log.e(TAG, EXCEPTION_MESSAGE + e.stackTraceToString())
            BasicResponse.Error(NO_INTERNET_CONNECTION_ERROR)
        } catch (e: SocketTimeoutException) {
            Log.e(TAG, EXCEPTION_MESSAGE + e.stackTraceToString())
            BasicResponse.Error(SOCKET_TIMEOUT_ERROR)
        } catch (e: Exception) {
            Log.e(TAG, EXCEPTION_MESSAGE + e.stackTraceToString())
            BasicResponse.Error(UNKNOWN_ERROR)
        }
    }

    override suspend fun authenticate(token: String): BasicResponse<String> {
        return try {
            val response = client.get(ENDPOINT_AUTH) {
                bearerAuth(token)
            }
            when (response.status.value) {
                200 -> {
                    BasicResponse.Success()
                }
                401 -> {
                    Log.e(TAG, "Token is invalid")
                    BasicResponse.Error(UNAUTHENTICATED)
                }
                else -> {
                    Log.e(TAG, NET_EXCEPTION_MESSAGE + response.status.value + response.status.description)
                    BasicResponse.Error(UNKNOWN_ERROR)
                }
            }
        } catch (e: ConnectException) {
            Log.e(TAG, EXCEPTION_MESSAGE + e.stackTraceToString())
            BasicResponse.Error(NO_INTERNET_CONNECTION_ERROR)
        } catch (e: SocketTimeoutException) {
            Log.e(TAG, EXCEPTION_MESSAGE + e.stackTraceToString())
            BasicResponse.Error(SOCKET_TIMEOUT_ERROR)
        } catch (e: Exception) {
            Log.e(TAG, EXCEPTION_MESSAGE + e.stackTraceToString())
            BasicResponse.Error(UNKNOWN_ERROR)
        }
    }
}
