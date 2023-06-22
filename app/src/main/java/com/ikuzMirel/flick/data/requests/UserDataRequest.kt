package com.ikuzMirel.flick.data.requests

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserDataRequest(
    @SerialName("id")
    val id: String,
    @SerialName("token")
    val token: String
)
