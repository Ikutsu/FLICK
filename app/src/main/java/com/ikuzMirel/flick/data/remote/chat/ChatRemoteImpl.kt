package com.ikuzMirel.flick.data.remote.chat

import android.util.Log
import com.ikuzMirel.flick.data.dto.chat.request.MessageListRequestDto
import com.ikuzMirel.flick.data.dto.chat.response.MessageListResponseDto
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

class ChatRemoteImpl @Inject constructor(
    private val client: HttpClient
): ChatRemote {
    override suspend fun getChatMessages(request: MessageListRequestDto): ResponseResult<MessageListResponseDto> {
        return try {
            val response = client.get(ENDPOINT_CHAT_MESSAGES){
                parameter("id", request.collectionId)
                bearerAuth(request.token)
            }
            when (response.status.value) {
                200 -> {
                    ResponseResult.Success(MessageListResponseDto(data = response.body<MessageListResponseDto.Data>()))
                }
                401 -> {
                    ResponseResult.error(MessageListResponseDto(error = UNAUTHENTICATED))
                }
                else -> {
                    Log.e(TAG, NET_EXCEPTION_MESSAGE + response.status.value + response.status.description)
                    ResponseResult.error(MessageListResponseDto(error = UNKNOWN_HTTP_ERROR))
                }
            }
        } catch (e: ConnectException) {
            Log.e(TAG, EXCEPTION_MESSAGE + e.stackTraceToString())
            ResponseResult.error(MessageListResponseDto(error = NO_INTERNET_CONNECTION_ERROR))
        } catch (e: SocketTimeoutException) {
            Log.e(TAG, EXCEPTION_MESSAGE + e.stackTraceToString())
            ResponseResult.error(MessageListResponseDto(error = SOCKET_TIMEOUT_ERROR))
        } catch (e: Exception) {
            Log.e(TAG, EXCEPTION_MESSAGE + e.stackTraceToString())
            ResponseResult.error(MessageListResponseDto(error = UNKNOWN_ERROR))
        }
    }
}