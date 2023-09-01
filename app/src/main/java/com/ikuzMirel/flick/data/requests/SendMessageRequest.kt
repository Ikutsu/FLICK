package com.ikuzMirel.flick.data.requests

import kotlinx.serialization.Serializable

@Serializable
data class SendMessageRequest(
    val message: String,
    val collectionId: String,
    val receiverId: String,
    val id: String
)