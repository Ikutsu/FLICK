package com.ikuzMirel.flick.data.remote.user

import android.util.Log
import com.ikuzMirel.flick.data.dto.friendList.request.FriendListRequestDto
import com.ikuzMirel.flick.data.dto.friendList.response.FriendListResponseDto
import com.ikuzMirel.flick.data.dto.userData.request.UserDataRequestDto
import com.ikuzMirel.flick.data.dto.userData.response.UserDataResponseDto
import com.ikuzMirel.flick.data.utils.*
import com.ikuzMirel.flick.utils.EXCEPTION_MESSAGE
import com.ikuzMirel.flick.utils.NET_EXCEPTION_MESSAGE
import com.ikuzMirel.flick.utils.TAG
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import java.net.ConnectException
import java.net.SocketTimeoutException
import javax.inject.Inject

class UserRemoteImpl @Inject constructor(
    private val client: HttpClient
): UserRemote {
    override suspend fun getUserInfo(request: UserDataRequestDto): ResponseResult<UserDataResponseDto> {
        return try {
            val response = client.get(ENDPOINT_USER_INFO){
                parameter("id", request.id)
                bearerAuth(request.token)
            }
            when (response.status.value){
                200 -> {
                    ResponseResult.Success(UserDataResponseDto(data = response.body<UserDataResponseDto.Data>()))
                }
                401 -> {
                    ResponseResult.error(UserDataResponseDto(error = UNAUTHENTICATED))
                }
                409 -> {
                    ResponseResult.error(UserDataResponseDto(error = USER_NOT_EXIST))
                }
                else -> {
                    Log.e(TAG, NET_EXCEPTION_MESSAGE + response.status.value + response.status.description)
                    ResponseResult.error(UserDataResponseDto(error = UNKNOWN_HTTP_ERROR))
                }
            }
        } catch (e: ConnectException) {
            Log.e(TAG, EXCEPTION_MESSAGE + e.stackTraceToString())
            ResponseResult.error(UserDataResponseDto(error = NO_INTERNET_CONNECTION_ERROR))
        } catch (e: SocketTimeoutException) {
            Log.e(TAG, EXCEPTION_MESSAGE + e.stackTraceToString())
            ResponseResult.error(UserDataResponseDto(error = SOCKET_TIMEOUT_ERROR))
        } catch (e: Exception) {
            Log.e(TAG, EXCEPTION_MESSAGE + e.stackTraceToString())
            ResponseResult.error(UserDataResponseDto(error = UNKNOWN_ERROR))
        }
    }

    override suspend fun getUserFriends(request: FriendListRequestDto): ResponseResult<FriendListResponseDto> {
        return try {
            val response = client.get(ENDPOINT_USER_FRIENDS){
                parameter("id", request.id)
                bearerAuth(request.token)
            }
            when (response.status.value) {
                200 -> {
                    ResponseResult.Success(FriendListResponseDto(data = response.body<FriendListResponseDto.Data>()))
                }
                401 -> {
                    ResponseResult.error(FriendListResponseDto(error = UNAUTHENTICATED))
                }
                409 -> {
                    ResponseResult.error(FriendListResponseDto(error = USER_NOT_EXIST))
                }
                else -> {
                    Log.e(TAG, NET_EXCEPTION_MESSAGE + response.status.value + response.status.description)
                    ResponseResult.error(FriendListResponseDto(error = UNKNOWN_HTTP_ERROR))
                }
            }
        } catch (e: ConnectException) {
            Log.e(TAG, EXCEPTION_MESSAGE + e.stackTraceToString())
            ResponseResult.error(FriendListResponseDto(error = NO_INTERNET_CONNECTION_ERROR))
        } catch (e: SocketTimeoutException) {
            Log.e(TAG, EXCEPTION_MESSAGE + e.stackTraceToString())
            ResponseResult.error(FriendListResponseDto(error = SOCKET_TIMEOUT_ERROR))
        } catch (e: Exception) {
            Log.e(TAG, EXCEPTION_MESSAGE + e.stackTraceToString())
            ResponseResult.error(FriendListResponseDto(error = UNKNOWN_ERROR))
        }
    }
}