package com.ikuzMirel.flick.data.requests

import kotlinx.serialization.Serializable

@Serializable
data class SendFriendReqRequest(
    val senderId: String,
    val receiverId: String
)
