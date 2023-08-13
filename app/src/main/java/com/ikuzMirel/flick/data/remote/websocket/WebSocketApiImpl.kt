package com.ikuzMirel.flick.data.remote.websocket

import android.util.Log
import com.ikuzMirel.flick.data.constants.ENDPOINT_WEBSOCKET
import com.ikuzMirel.flick.data.constants.WEBSOCKET_CONNECTION_ERROR
import com.ikuzMirel.flick.data.response.BasicResponse
import com.ikuzMirel.flick.data.response.WebSocketResponse
import io.ktor.client.HttpClient
import io.ktor.client.plugins.websocket.webSocketSession
import io.ktor.client.request.parameter
import io.ktor.client.request.url
import io.ktor.websocket.Frame
import io.ktor.websocket.WebSocketSession
import io.ktor.websocket.close
import io.ktor.websocket.readText
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.isActive
import kotlinx.serialization.json.Json
import javax.inject.Inject

class WebSocketApiImpl @Inject constructor(
    private val client: HttpClient
) : WebSocketApi {

    private var webSocketSession: WebSocketSession? = null

    override suspend fun connectToSocket(userId: String): BasicResponse<String> {
        return try {
            println( "connectToSocket: userId: $userId")
            webSocketSession = client.webSocketSession {
                url(ENDPOINT_WEBSOCKET).apply {
                    parameter("Uid", userId)
                }
            }
            if (webSocketSession?.isActive == true) {
                Log.d("WebSocket", "Connected")
                BasicResponse.Success()
            } else {
                BasicResponse.Error(WEBSOCKET_CONNECTION_ERROR)
            }
        } catch (e: Exception) {
            BasicResponse.Error(e.message ?: "")
        }
    }

    override suspend fun disconnectFromSocket() {
        webSocketSession?.close()
        Log.d("WebSocket", "Disconnected")
    }

    override suspend fun sendMessage(message: String): BasicResponse<String> {
        try {
            webSocketSession?.send(Frame.Text(message))
            return BasicResponse.Success()
        } catch (e: Exception) {
            e.printStackTrace()
            return BasicResponse.Error(e.message ?: "")
        }
    }

    override fun receiveMessage(): Flow<WebSocketResponse> =
        try {
            webSocketSession?.incoming
                ?.receiveAsFlow()
                ?.filter { it is Frame.Text }
                ?.map {
                    val json = (it as? Frame.Text)?.readText().orEmpty()
                    val message = Json.decodeFromString<WebSocketResponse>(json)
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