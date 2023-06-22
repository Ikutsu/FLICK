package com.ikuzMirel.flick.data.repositories

import com.ikuzMirel.flick.data.model.UserData
import com.ikuzMirel.flick.data.requests.FriendListRequest
import com.ikuzMirel.flick.data.requests.UserDataRequest
import com.ikuzMirel.flick.data.response.BasicResponse
import com.ikuzMirel.flick.data.response.FriendListResponse
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun getUserInfo(request: UserDataRequest): Flow<BasicResponse<UserData>>
    suspend fun gerUserFriends(request: FriendListRequest): Flow<BasicResponse<FriendListResponse>>
}