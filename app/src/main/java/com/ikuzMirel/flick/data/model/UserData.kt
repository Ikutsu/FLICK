package com.ikuzMirel.flick.data.model

@kotlinx.serialization.Serializable
data class UserData(
    val username: String,
    val email: String,
)
