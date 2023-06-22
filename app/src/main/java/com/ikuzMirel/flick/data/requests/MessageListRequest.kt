package com.ikuzMirel.flick.data.requests

import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class MessageListRequest(
    @SerialName("id")
    val collectionId: String,
    @SerialName("token")
    val token: String
)
