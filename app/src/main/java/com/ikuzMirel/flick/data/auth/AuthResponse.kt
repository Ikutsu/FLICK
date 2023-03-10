package com.ikuzMirel.flick.data.auth

data class AuthResponse(
    val token: String,
    val username: String,
    val userId: String,
)