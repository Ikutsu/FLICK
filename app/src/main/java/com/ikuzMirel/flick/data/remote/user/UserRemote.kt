package com.ikuzMirel.flick.data.remote.user

import com.ikuzMirel.flick.data.model.UserData
import com.ikuzMirel.flick.data.requests.FriendListRequest
import com.ikuzMirel.flick.data.requests.SearchUsersRequest
import com.ikuzMirel.flick.data.requests.UserDataRequest
import com.ikuzMirel.flick.data.response.BasicResponse
import com.ikuzMirel.flick.data.response.FriendListResponse
import com.ikuzMirel.flick.data.response.UserListResponse
import com.ikuzMirel.flick.domain.entities.FriendEntity

interface UserRemote {
    suspend fun getUserInfo(
        request: UserDataRequest
    ): BasicResponse<UserData>

    suspend fun getUserFriends(
        request: FriendListRequest
    ): BasicResponse<FriendListResponse>

    suspend fun getUserFriend(
        userId: String,
        friendUserId: String,
        token: String
    ): BasicResponse<FriendEntity>

    suspend fun searchUsers(
        request: SearchUsersRequest
    ): BasicResponse<UserListResponse>
}