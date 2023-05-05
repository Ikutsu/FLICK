package com.ikuzMirel.flick.data.dto.chat.request

import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class MessageListRequestDto(
    @SerialName("id")
    val collectionId: String,
    @SerialName("token")
    val token: String
)
