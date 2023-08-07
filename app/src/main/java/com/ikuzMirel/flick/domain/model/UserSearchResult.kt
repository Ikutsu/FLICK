package com.ikuzMirel.flick.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class UserSearchResult(
    val userId: String,
    val username: String,
    val friendWithMe: Boolean,
    val collectionId: String
)