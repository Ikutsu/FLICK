package com.ikuzMirel.flick.data.repositories

import com.ikuzMirel.flick.data.remote.friendRequest.FriendRequestRemote
import com.ikuzMirel.flick.data.requests.SendFriendReqRequest
import com.ikuzMirel.flick.data.response.BasicResponse
import com.ikuzMirel.flick.data.response.FriendRequestListResponse
import com.ikuzMirel.flick.domain.entities.FriendRequestEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class FriendReqRepositoryImpl @Inject constructor(
    private val remote: FriendRequestRemote
) : FriendReqRepository {

    override suspend fun sendFriendRequest(
        request: SendFriendReqRequest
    ): Flow<BasicResponse<FriendRequestEntity>> {
        return flow {
            when (val response = remote.sendFriendRequest(request)) {
                is BasicResponse.Success -> {
                    emit(BasicResponse.Success(response.data!!))
                }

                is BasicResponse.Error -> {
                    emit(BasicResponse.Error(response.errorMessage))
                }
            }
        }
    }

    override suspend fun cancelFriendRequest(
        request: String
    ): Flow<BasicResponse<FriendRequestEntity>> {
        return flow {
            when (val response = remote.cancelFriendRequest(request)) {
                is BasicResponse.Success -> {
                    emit(BasicResponse.Success())
                }

                is BasicResponse.Error -> {
                    emit(BasicResponse.Error(response.errorMessage))
                }
            }
        }
    }

    override suspend fun acceptFriendRequest(
        request: String
    ): Flow<BasicResponse<FriendRequestEntity>> {
        return flow {
            when (val response = remote.acceptFriendRequest(request)) {
                is BasicResponse.Success -> {
                    emit(BasicResponse.Success())
                }

                is BasicResponse.Error -> {
                    emit(BasicResponse.Error(response.errorMessage))
                }
            }
        }
    }

    override suspend fun rejectFriendRequest(
        request: String
    ): Flow<BasicResponse<FriendRequestEntity>> {
        return flow {
            when (val response = remote.rejectFriendRequest(request)) {
                is BasicResponse.Success -> {
                    emit(BasicResponse.Success())
                }

                is BasicResponse.Error -> {
                    emit(BasicResponse.Error(response.errorMessage))
                }
            }
        }
    }

    override suspend fun getAllSentFriendRequests(): Flow<BasicResponse<FriendRequestListResponse>> {
        return flow {
            when (val response = remote.getAllSentFriendRequests()) {
                is BasicResponse.Success -> {
                    emit(BasicResponse.Success(response.data))
                }

                is BasicResponse.Error -> {
                    emit(BasicResponse.Error(response.errorMessage))
                }
            }
        }
    }

    override suspend fun getAllReceivedFriendRequests(): Flow<BasicResponse<FriendRequestListResponse>> {
        return flow {
            when (val response = remote.getAllReceivedFriendRequests()) {
                is BasicResponse.Success -> {
                    emit(BasicResponse.Success(response.data))
                }

                is BasicResponse.Error -> {
                    emit(BasicResponse.Error(response.errorMessage))
                }
            }
        }
    }
}