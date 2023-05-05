package com.ikuzMirel.flick.data.repositories

import com.ikuzMirel.flick.data.dto.friendList.request.FriendListRequestDto
import com.ikuzMirel.flick.data.dto.friendList.response.FriendListResponseDto
import com.ikuzMirel.flick.data.dto.userData.request.UserDataRequestDto
import com.ikuzMirel.flick.data.dto.userData.response.UserDataResponseDto
import com.ikuzMirel.flick.data.utils.ResponseResult
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun getUserInfo(request: UserDataRequestDto): Flow<ResponseResult<UserDataResponseDto>>
    suspend fun gerUserFriends(request: FriendListRequestDto): Flow<ResponseResult<FriendListResponseDto>>
}