package com.ikuzMirel.flick.data.dto.friendList.response

import kotlinx.serialization.Serializable

@Serializable
data class FriendListResponseDto(
    val data: Data? = null,
    val error: String? = null
) {
    @Serializable
    data class Data(
        val friends: List<Friend>
    ){
        @Serializable
        data class Friend(
            val username: String,
            val id: String,
            val collectionId: String
        )
    }
}
