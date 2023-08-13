package com.ikuzMirel.flick.data.remote.websocket

import com.ikuzMirel.flick.data.response.BasicResponse
import com.ikuzMirel.flick.data.response.WebSocketResponse
import kotlinx.coroutines.flow.Flow

interface WebSocketApi {
    suspend fun connectToSocket(userId: String): BasicResponse<String>
    suspend fun disconnectFromSocket()
    suspend fun sendMessage(message: String): BasicResponse<String>
    fun receiveMessage(): Flow<WebSocketResponse>
    fun checkConnection(): Boolean
}