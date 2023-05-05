package com.ikuzMirel.flick.data.remote.websocket

import com.ikuzMirel.flick.data.dto.chat.response.MessageListResponseDto
import com.ikuzMirel.flick.data.dto.chat.response.ReceivedMessageDto
import com.ikuzMirel.flick.data.utils.ENDPOINT_WEBSOCKET
import com.ikuzMirel.flick.data.utils.ResponseResult
import io.ktor.client.*
import io.ktor.client.plugins.websocket.*
import io.ktor.client.request.*
import io.ktor.websocket.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.isActive
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import javax.inject.Inject

class WebSocketServiceImpl @Inject constructor(
    private val client: HttpClient
) : WebSocketService {

    private var webSocketSession: WebSocketSession? = null

    override suspend fun connectToSocket(userId: String, token: String): ResponseResult<String> {
        return try {
            webSocketSession = client.webSocketSession {
                url(ENDPOINT_WEBSOCKET).apply {
                    parameter("Uid", userId)
                    bearerAuth(token)
                }
            }
            if (webSocketSession?.isActive == true) {
                println("Websocket: CONNECTED")
                ResponseResult.success()
            } else {
                ResponseResult.error("Couldn't establish connection")
            }
        } catch (e: Exception) {
            ResponseResult.error(e.message ?: "")
        }
    }

    override suspend fun disconnectFromSocket() {
        webSocketSession?.close()
        println("Socket closed")
    }

    override suspend fun sendMessage(message: String) {
        try {
            webSocketSession?.send(Frame.Text(message))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun receiveMessage(): Flow<ReceivedMessageDto> =
        try {
            webSocketSession?.incoming
                ?.receiveAsFlow()
                ?.filter { it is Frame.Text }
                ?.map {
                    val json = (it as? Frame.Text)?.readText().orEmpty()
                    val message = Json.decodeFromString<ReceivedMessageDto>(json)
                    message
                }?: flow {  }
        } catch (e: Exception) {
            e.printStackTrace()
            emptyFlow()
        }

    override fun checkConnection(): Boolean {
        return webSocketSession?.isActive == true
    }
}