package com.ikuzMirel.flick.data.dto.friendList.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FriendListRequestDto(
    @SerialName("id")
    val id: String,
    @SerialName("token")
    val token: String
)
