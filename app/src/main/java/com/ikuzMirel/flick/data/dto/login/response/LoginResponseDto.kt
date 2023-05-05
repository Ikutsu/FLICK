package com.ikuzMirel.flick.data.dto.login.response

import kotlinx.serialization.Serializable

@Serializable
data class LoginResponseDto(
    val data: Data? = null,
    val error: String? = null
) {
    @Serializable
    data class Data(
        val token: String,
        val username: String,
        val userId: String
    )
}