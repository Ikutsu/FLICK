package com.ikuzMirel.flick.data.repositories

import com.ikuzMirel.flick.data.model.UserData
import com.ikuzMirel.flick.data.requests.FriendListRequest
import com.ikuzMirel.flick.data.requests.SearchUsersRequest
import com.ikuzMirel.flick.data.requests.UserDataRequest
import com.ikuzMirel.flick.data.response.BasicResponse
import com.ikuzMirel.flick.data.response.FriendListResponse
import com.ikuzMirel.flick.data.response.UserListResponse
import com.ikuzMirel.flick.domain.entities.FriendEntity
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun getUserInfo(request: UserDataRequest): Flow<BasicResponse<UserData>>
    suspend fun getUserFriends(request: FriendListRequest): Flow<BasicResponse<FriendListResponse>>
    suspend fun getUserFriend(userId: String, friendUserId: String, token: String): Flow<BasicResponse<FriendEntity>>
    suspend fun searchUsers(request: SearchUsersRequest): Flow<BasicResponse<UserListResponse>>
}