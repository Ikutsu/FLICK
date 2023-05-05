package com.ikuzMirel.flick.data.dto.userData.response

import kotlinx.serialization.Serializable

@Serializable
data class UserDataResponseDto(
    val data: Data? = null,
    val error: String? = null
) {
    @Serializable
    data class Data (
        val username: String,
        val email: String,
    )
}
