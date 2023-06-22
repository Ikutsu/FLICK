package com.ikuzMirel.flick.data.response

import com.ikuzMirel.flick.domain.entities.FriendEntity
import kotlinx.serialization.Serializable

@Serializable
data class FriendListResponse(
    val friends: List<FriendEntity>
)
