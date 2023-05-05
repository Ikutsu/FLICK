package com.ikuzMirel.flick.data.repositories

import com.ikuzMirel.flick.data.dto.friendList.request.FriendListRequestDto
import com.ikuzMirel.flick.data.dto.friendList.response.FriendListResponseDto
import com.ikuzMirel.flick.data.dto.userData.request.UserDataRequestDto
import com.ikuzMirel.flick.data.dto.userData.response.UserDataResponseDto
import com.ikuzMirel.flick.data.remote.user.UserRemote
import com.ikuzMirel.flick.data.utils.ResponseResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val remote: UserRemote
) : UserRepository {
    override suspend fun getUserInfo(request: UserDataRequestDto): Flow<ResponseResult<UserDataResponseDto>> {
        return flow {
            when (val response = remote.getUserInfo(request)) {
                is ResponseResult.Error -> {
                    emit(ResponseResult.error(response.errorMessage))
                }
                is ResponseResult.Success -> {
                    emit(ResponseResult.success(response.data))
                }
            }
        }
    }

    override suspend fun gerUserFriends(request: FriendListRequestDto): Flow<ResponseResult<FriendListResponseDto>> {
        return flow {
            when (val response = remote.getUserFriends(request)) {
                is ResponseResult.Error -> {
                    emit(ResponseResult.error(response.errorMessage))
                }
                is ResponseResult.Success -> {
                    emit(ResponseResult.success(response.data))
                }
            }
        }
    }
}