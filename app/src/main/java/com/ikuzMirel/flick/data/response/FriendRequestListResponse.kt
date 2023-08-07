package com.ikuzMirel.flick.data.response

import com.ikuzMirel.flick.domain.entities.FriendRequestEntity
import kotlinx.serialization.Serializable

@Serializable
data class FriendRequestListResponse(
    val friendRequests: List<FriendRequestEntity>
)
