package com.ikuzMirel.flick.data.response

import com.ikuzMirel.flick.domain.model.UserSearchResult
import kotlinx.serialization.Serializable

@Serializable
data class UserListResponse(
    val users: List<UserSearchResult>
)