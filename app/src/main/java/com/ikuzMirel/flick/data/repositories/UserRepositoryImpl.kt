package com.ikuzMirel.flick.data.repositories

import com.ikuzMirel.flick.data.model.UserData
import com.ikuzMirel.flick.data.remote.user.UserRemote
import com.ikuzMirel.flick.data.requests.FriendListRequest
import com.ikuzMirel.flick.data.requests.SearchUsersRequest
import com.ikuzMirel.flick.data.requests.UserDataRequest
import com.ikuzMirel.flick.data.response.BasicResponse
import com.ikuzMirel.flick.data.response.FriendListResponse
import com.ikuzMirel.flick.data.response.UserListResponse
import com.ikuzMirel.flick.domain.entities.FriendEntity
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

    override suspend fun getUserFriend(
        userId: String,
        friendUserId: String,
        token: String
    ): Flow<BasicResponse<FriendEntity>> {
        return flow {
            when (val response = remote.getUserFriend(userId, friendUserId, token)) {
                is BasicResponse.Error -> {
                    emit(BasicResponse.Error(response.errorMessage))
                }
                is BasicResponse.Success -> {
                    emit(BasicResponse.Success(response.data))
                }
            }
        }
    }

    override suspend fun getUserFriends(request: FriendListRequest): Flow<BasicResponse<FriendListResponse>> {
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

    override suspend fun searchUsers(request: SearchUsersRequest): Flow<BasicResponse<UserListResponse>> {
        return flow {
            when (val response = remote.searchUsers(request)) {
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