package com.ikuzMirel.flick.data.repositories

import com.ikuzMirel.flick.data.model.UserData
import com.ikuzMirel.flick.data.remote.user.UserRemote
import com.ikuzMirel.flick.data.requests.FriendListRequest
import com.ikuzMirel.flick.data.requests.UserDataRequest
import com.ikuzMirel.flick.data.response.BasicResponse
import com.ikuzMirel.flick.data.response.FriendListResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val remote: UserRemote
) : UserRepository {
    override suspend fun getUserInfo(request: UserDataRequest): Flow<BasicResponse<UserData>> {
        return flow {
            when (val response = remote.getUserInfo(request)) {
                is BasicResponse.Error -> {
                    emit(BasicResponse.Error(response.errorMessage))
                }
                is BasicResponse.Success -> {
                    emit(BasicResponse.Success(response.data))
                }
            }
        }
    }

    override suspend fun gerUserFriends(request: FriendListRequest): Flow<BasicResponse<FriendListResponse>> {
        return flow {
            when (val response = remote.getUserFriends(request)) {
                is BasicResponse.Error -> {
                    emit(BasicResponse.Error(response.errorMessage))
                }
                is BasicResponse.Success -> {
                    emit(BasicResponse.Success(response.data))
                }
            }
        }
    }
}