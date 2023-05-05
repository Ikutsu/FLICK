package com.ikuzMirel.flick.data.remote.auth

import android.util.Log
import com.ikuzMirel.flick.data.dto.login.request.LoginRequestDto
import com.ikuzMirel.flick.data.dto.login.response.LoginResponseDto
import com.ikuzMirel.flick.data.dto.signup.request.SignupRequestDto
import com.ikuzMirel.flick.data.utils.*
import com.ikuzMirel.flick.utils.EXCEPTION_MESSAGE
import com.ikuzMirel.flick.utils.NET_EXCEPTION_MESSAGE
import com.ikuzMirel.flick.utils.TAG
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.*
import io.ktor.client.request.*
import java.net.ConnectException
import java.net.SocketTimeoutException
import javax.inject.Inject

class AuthRemoteImpl @Inject constructor(
    private val client: HttpClient
) : AuthRemote {
    override suspend fun login(request: LoginRequestDto): ResponseResult<LoginResponseDto> {
        return try {
            val response = client.post(ENDPOINT_LOGIN) {
                setBody(request)
            }
            println(response.body<LoginResponseDto.Data>())
            when (response.status.value) {
                200 -> {
                    ResponseResult.success(LoginResponseDto(data = response.body<LoginResponseDto.Data>()))
                }
                401 -> {
                    ResponseResult.error(LoginResponseDto(error = UNAUTHENTICATED))
                }
                409 -> {
                    ResponseResult.error(LoginResponseDto(error = LOGIN_CONFLICTED))
                }
                else -> {
                    Log.e(TAG, NET_EXCEPTION_MESSAGE + response.status.value + response.status.description)
                    ResponseResult.error(LoginResponseDto(error = UNKNOWN_HTTP_ERROR))
                }
            }
        } catch (e: ConnectException) {
            Log.e(TAG, EXCEPTION_MESSAGE + e.stackTraceToString())
            ResponseResult.error(LoginResponseDto(error = NO_INTERNET_CONNECTION_ERROR))
        } catch (e: SocketTimeoutException) {
            Log.e(TAG, EXCEPTION_MESSAGE + e.stackTraceToString())
            ResponseResult.error(LoginResponseDto(error = SOCKET_TIMEOUT_ERROR))
        } catch (e: Exception) {
            Log.e(TAG, EXCEPTION_MESSAGE + e.stackTraceToString())
            ResponseResult.error(LoginResponseDto(error = UNKNOWN_ERROR))
        }
    }

    override suspend fun signup(request: SignupRequestDto): ResponseResult<String> {
        return try {
            val response = client.post(ENDPOINT_REGISTER) {
                setBody(request)
            }
            when (response.status.value) {
                200 -> {
                    ResponseResult.success()
                }
                401 -> {
                    ResponseResult.error(UNAUTHENTICATED)
                }
                409 -> {
                    ResponseResult.error(USERNAME_CONFLICTED)
                }
                else -> {
                    Log.e(TAG, NET_EXCEPTION_MESSAGE + response.status.value + response.status.description)
                    ResponseResult.error(UNKNOWN_HTTP_ERROR)
                }
            }
        } catch (e: ConnectException) {
            Log.e(TAG, EXCEPTION_MESSAGE + e.stackTraceToString())
            ResponseResult.error(NO_INTERNET_CONNECTION_ERROR)
        } catch (e: SocketTimeoutException) {
            Log.e(TAG, EXCEPTION_MESSAGE + e.stackTraceToString())
            ResponseResult.error(SOCKET_TIMEOUT_ERROR)
        } catch (e: Exception) {
            Log.e(TAG, EXCEPTION_MESSAGE + e.stackTraceToString())
            ResponseResult.error(UNKNOWN_ERROR)
        }
    }

    override suspend fun authenticate(token: String): ResponseResult<String> {
        return try {
            val response = client.get(ENDPOINT_AUTH) {
                bearerAuth(token)
            }
            when (response.status.value) {
                200 -> {
                    ResponseResult.success()
                }
                401 -> {
                    Log.e(TAG, "Token is invalid")
                    ResponseResult.error(UNAUTHENTICATED)
                }
                else -> {
                    Log.e(TAG, NET_EXCEPTION_MESSAGE + response.status.value + response.status.description)
                    ResponseResult.error(UNKNOWN_ERROR)
                }
            }
        } catch (e: ConnectException) {
            Log.e(TAG, EXCEPTION_MESSAGE + e.stackTraceToString())
            ResponseResult.error(NO_INTERNET_CONNECTION_ERROR)
        } catch (e: SocketTimeoutException) {
            Log.e(TAG, EXCEPTION_MESSAGE + e.stackTraceToString())
            ResponseResult.error(SOCKET_TIMEOUT_ERROR)
        } catch (e: Exception) {
            Log.e(TAG, EXCEPTION_MESSAGE + e.stackTraceToString())
            ResponseResult.error(UNKNOWN_ERROR)
        }
    }
}
