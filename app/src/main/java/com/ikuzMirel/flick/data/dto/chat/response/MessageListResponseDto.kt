package com.ikuzMirel.flick.data.dto.chat.response

import kotlinx.serialization.Serializable

@Serializable
data class MessageListResponseDto(
    val data: Data? = null,
    val error: String? = null
){
    @Serializable
    data class Data(
        val messages: List<Message>
    ){
        @Serializable
        data class Message(
            val content: String,
            val senderUid: String,
            val senderUsername: String,
            val timestamp: Long,
            val id: String
        )
    }
}
