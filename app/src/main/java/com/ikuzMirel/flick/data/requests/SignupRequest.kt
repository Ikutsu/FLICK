package com.ikuzMirel.flick.data.requests

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SignupRequest(
    @SerialName("username")
    val username: String,
    @SerialName("password")
    val password: String,
    @SerialName("email")
    val email: String
)
