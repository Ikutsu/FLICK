package com.ikuzMirel.flick.data.dto.userData.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserDataRequestDto(
    @SerialName("id")
    val id: String,
    @SerialName("token")
    val token: String
)
