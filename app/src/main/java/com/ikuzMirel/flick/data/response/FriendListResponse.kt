package com.ikuzMirel.flick.data.response

import kotlinx.serialization.Serializable

@Serializable
data class FriendListResponse(
    val friends: List<FriendResponse>
)
