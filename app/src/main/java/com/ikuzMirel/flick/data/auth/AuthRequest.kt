package com.ikuzMirel.flick.data.auth

data class AuthRequest(
    val username: String,
    val password: String,
    val email: String = ""
)
