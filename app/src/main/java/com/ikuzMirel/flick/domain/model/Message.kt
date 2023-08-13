package com.ikuzMirel.flick.domain.model

import com.ikuzMirel.flick.domain.entities.MessageEntity

data class Message(
    val content: String,
    val userId: String,
    val timestamp: String,
    val id: String,
    val state: String
)

fun Message.toMessageEntity(
    timestamp: Long,
    collectionId: String
) = MessageEntity (
    id = id,
    content = content,
    senderUid = userId,
    timestamp = timestamp,
    collectionId = collectionId,
    state = state
)
enum class MessageState {
    SENDING,
    SENT,
    ERROR
}
