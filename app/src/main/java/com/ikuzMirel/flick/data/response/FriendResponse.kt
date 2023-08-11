package com.ikuzMirel.flick.data.response

import kotlinx.serialization.Serializable

@Serializable
data class FriendResponse(
    val username: String,
    val userId: String,
    val collectionId: String
)
