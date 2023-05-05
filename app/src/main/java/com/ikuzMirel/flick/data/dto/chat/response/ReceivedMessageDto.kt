package com.ikuzMirel.flick.data.dto.chat.response

@kotlinx.serialization.Serializable
data class ReceivedMessageDto(
    val content: String,
    val senderUid: String,
    val senderUsername: String,
    val timestamp: Long,
    val id: String,
    val cid: String
)