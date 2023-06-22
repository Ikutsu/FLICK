package com.ikuzMirel.flick.data.remote.chat

import android.util.Log
import com.ikuzMirel.flick.data.constants.ENDPOINT_CHAT_MESSAGES
import com.ikuzMirel.flick.data.constants.NO_INTERNET_CONNECTION_ERROR
import com.ikuzMirel.flick.data.constants.SOCKET_TIMEOUT_ERROR
import com.ikuzMirel.flick.data.constants.UNAUTHENTICATED
import com.ikuzMirel.flick.data.constants.UNKNOWN_ERROR
import com.ikuzMirel.flick.data.constants.UNKNOWN_HTTP_ERROR
import com.ikuzMirel.flick.data.requests.MessageListRequest
import com.ikuzMirel.flick.data.response.BasicResponse
import com.ikuzMirel.flick.data.response.MessageListResponse
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

class ChatRemoteImpl @Inject constructor(
    private val client: HttpClient
): ChatRemote {
    override suspend fun getChatMessages(request: MessageListRequest): BasicResponse<MessageListResponse> {
        return try {
            val response = client.get(ENDPOINT_CHAT_MESSAGES){
                parameter("id", request.collectionId)
                bearerAuth(request.token)
            }
            when (response.status.value) {
                200 -> {
                    BasicResponse.Success( response.body<MessageListResponse>() )
                }
                401 -> {
                    BasicResponse.Error(UNAUTHENTICATED)
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
}