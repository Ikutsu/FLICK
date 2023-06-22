package com.ikuzMirel.flick.data.requests

import kotlinx.serialization.Serializable

@Serializable
data class SendMessageRequest(
    val message: String,
    val cid: String,
    val receiverId: String
)