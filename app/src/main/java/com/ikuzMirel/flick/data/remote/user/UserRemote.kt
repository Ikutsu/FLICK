package com.ikuzMirel.flick.data.remote.user

import com.ikuzMirel.flick.data.model.UserData
import com.ikuzMirel.flick.data.response.BasicResponse
import com.ikuzMirel.flick.data.response.FriendListResponse
import com.ikuzMirel.flick.data.response.UserListResponse
import com.ikuzMirel.flick.domain.entities.FriendEntity

interface UserRemote {
    suspend fun getUserInfo(
        userId: String
    ): BasicResponse<UserData>

    suspend fun getUserFriends(): BasicResponse<FriendListResponse>

    suspend fun getUserFriend(
        friendUserId: String
    ): BasicResponse<FriendEntity>

    suspend fun searchUsers(
        searchQuery: String
    ): BasicResponse<UserListResponse>
}