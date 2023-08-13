package com.ikuzMirel.flick.data.response

import com.ikuzMirel.flick.domain.entities.MessageEntity
import com.ikuzMirel.flick.domain.model.MessageState
import kotlinx.serialization.Serializable

@Serializable
data class MessageResponse(
    val id: String,
    val content: String,
    val senderUid: String,
    val timestamp: Long,
    val collectionId: String,
)

fun MessageResponse.toMessageEntity() = MessageEntity(
    id = id,
    content = content,
    senderUid = senderUid,
    timestamp = timestamp,
    collectionId = collectionId,
    state = MessageState.SENT.name
)