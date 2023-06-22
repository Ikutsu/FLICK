package com.ikuzMirel.flick.data.remote.user

import android.util.Log
import com.ikuzMirel.flick.data.constants.ENDPOINT_USER_FRIENDS
import com.ikuzMirel.flick.data.constants.ENDPOINT_USER_INFO
import com.ikuzMirel.flick.data.constants.NO_INTERNET_CONNECTION_ERROR
import com.ikuzMirel.flick.data.constants.SOCKET_TIMEOUT_ERROR
import com.ikuzMirel.flick.data.constants.UNAUTHENTICATED
import com.ikuzMirel.flick.data.constants.UNKNOWN_ERROR
import com.ikuzMirel.flick.data.constants.UNKNOWN_HTTP_ERROR
import com.ikuzMirel.flick.data.constants.USER_NOT_EXIST
import com.ikuzMirel.flick.data.model.UserData
import com.ikuzMirel.flick.data.requests.FriendListRequest
import com.ikuzMirel.flick.data.requests.UserDataRequest
import com.ikuzMirel.flick.data.response.BasicResponse
import com.ikuzMirel.flick.data.response.FriendListResponse
import com.ikuzMirel.flick.utils.EXCEPTION_MESSAGE
import com.ikuzMirel.flick.utils.NET_EXCEPTION_MESSAGE
import com.ikuzMirel.flick.utils.TAG
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import java.net.ConnectException
import java.net.SocketTimeoutException
import javax.inject.Inject

class UserRemoteImpl @Inject constructor(
    private val client: HttpClient
) : UserRemote {
    override suspend fun getUserInfo(request: UserDataRequest): BasicResponse<UserData> {
        return try {
            val response = client.get(ENDPOINT_USER_INFO) {
                parameter("id", request.id)
                bearerAuth(request.token)
            }
            when (response.status.value) {
                200 -> {
                    BasicResponse.Success(response.body<UserData>())
                }

                401 -> {
                    BasicResponse.Error(UNAUTHENTICATED)
                }

                409 -> {
                    BasicResponse.Error(USER_NOT_EXIST)
                }

                else -> {
                    Log.e(
                        TAG,
                        NET_EXCEPTION_MESSAGE + response.status.value + response.status.description
                    )
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

    override suspend fun getUserFriends(request: FriendListRequest): BasicResponse<FriendListResponse> {
        return try {
            val response = client.get(ENDPOINT_USER_FRIENDS) {
                parameter("id", request.id)
                bearerAuth(request.token)
            }
            when (response.status.value) {
                200 -> {
                    BasicResponse.Success(response.body<FriendListResponse>())
                }

                401 -> {
                    BasicResponse.Error(UNAUTHENTICATED)
                }

                409 -> {
                    BasicResponse.Error(USER_NOT_EXIST)
                }

                else -> {
                    Log.e(
                        TAG,
                        NET_EXCEPTION_MESSAGE + response.status.value + response.status.description
                    )
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
}