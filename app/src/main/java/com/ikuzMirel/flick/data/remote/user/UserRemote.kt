package com.ikuzMirel.flick.data.remote.user

import com.ikuzMirel.flick.data.dto.friendList.request.FriendListRequestDto
import com.ikuzMirel.flick.data.dto.friendList.response.FriendListResponseDto
import com.ikuzMirel.flick.data.dto.userData.request.UserDataRequestDto
import com.ikuzMirel.flick.data.dto.userData.response.UserDataResponseDto
import com.ikuzMirel.flick.data.utils.ResponseResult

interface UserRemote {
    suspend fun getUserInfo(
        request: UserDataRequestDto
    ): ResponseResult<UserDataResponseDto>

    suspend fun getUserFriends(
        request: FriendListRequestDto
    ): ResponseResult<FriendListResponseDto>
}