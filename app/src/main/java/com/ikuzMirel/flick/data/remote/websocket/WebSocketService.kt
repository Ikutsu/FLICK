package com.ikuzMirel.flick.data.remote.websocket

import com.ikuzMirel.flick.data.dto.chat.response.ReceivedMessageDto
import com.ikuzMirel.flick.data.utils.ResponseResult
import kotlinx.coroutines.flow.Flow

interface WebSocketService {
    suspend fun connectToSocket(userId: String, token: String): ResponseResult<String>
    suspend fun disconnectFromSocket()
    suspend fun sendMessage(message: String)
    fun receiveMessage(): Flow<ReceivedMessageDto>
    fun checkConnection(): Boolean
}