package com.ikuzMirel.flick.data.dto.chat.request

import kotlinx.serialization.Serializable

@Serializable
data class SendMessageDto(
    val message: String,
    val cid: String,
    val receiverId: String
)