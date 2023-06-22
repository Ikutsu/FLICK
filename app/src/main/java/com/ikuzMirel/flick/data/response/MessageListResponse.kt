package com.ikuzMirel.flick.data.response

import com.ikuzMirel.flick.domain.entities.MessageEntity
import kotlinx.serialization.Serializable

@Serializable
data class MessageListResponse(
    val messages: List<MessageEntity>
)
