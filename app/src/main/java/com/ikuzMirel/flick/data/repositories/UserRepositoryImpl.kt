package com.ikuzMirel.flick.data.repositories

import com.ikuzMirel.flick.data.model.UserData
import com.ikuzMirel.flick.data.remote.user.UserRemote
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
    override suspend fun getUserInfo(userId: String): Flow<BasicResponse<UserData>> {
        return flow {
            when (val response = remote.getUserInfo(userId)) {
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
        friendUserId: String
    ): Flow<BasicResponse<FriendEntity>> {
        return flow {
            when (val response = remote.getUserFriend(friendUserId)) {
                is BasicResponse.Error -> {
                    emit(BasicResponse.Error(response.errorMessage))
                }
                is BasicResponse.Success -> {
                    emit(BasicResponse.Success(response.data))
                }
            }
        }
    }

    override suspend fun getUserFriends(): Flow<BasicResponse<FriendListResponse>> {
        return flow {
            when (val response = remote.getUserFriends()) {
                is BasicResponse.Error -> {
                    emit(BasicResponse.Error(response.errorMessage))
                }
                is BasicResponse.Success -> {
                    emit(BasicResponse.Success(response.data))
                }
            }
        }
    }

    override suspend fun searchUsers(searchQuery: String): Flow<BasicResponse<UserListResponse>> {
        return flow {
            when (val response = remote.searchUsers(searchQuery)) {
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