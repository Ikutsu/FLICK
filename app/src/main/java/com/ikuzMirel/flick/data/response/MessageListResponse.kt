package com.ikuzMirel.flick.data.response

import kotlinx.serialization.Serializable

@Serializable
data class MessageListResponse(
    val messages: List<MessageResponse>
)
