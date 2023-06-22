package com.ikuzMirel.flick.data.model

@kotlinx.serialization.Serializable
data class AuthData(
    val token: String,
    val username: String,
    val userId: String
)
