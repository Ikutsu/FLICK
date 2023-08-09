package com.ikuzMirel.flick.data.repositories

import com.ikuzMirel.flick.data.requests.SendFriendReqRequest
import com.ikuzMirel.flick.data.response.BasicResponse
import com.ikuzMirel.flick.data.response.FriendRequestListResponse
import com.ikuzMirel.flick.domain.entities.FriendRequestEntity
import kotlinx.coroutines.flow.Flow

interface FriendReqRepository {
    suspend fun sendFriendRequest(
        request: SendFriendReqRequest
    ): Flow<BasicResponse<FriendRequestEntity>>

    suspend fun cancelFriendRequest(
        request: String
    ): Flow<BasicResponse<FriendRequestEntity>>

    suspend fun acceptFriendRequest(
        request: String
    ): Flow<BasicResponse<FriendRequestEntity>>

    suspend fun rejectFriendRequest(
        request: String
    ): Flow<BasicResponse<FriendRequestEntity>>

    suspend fun getAllSentFriendRequests(): Flow<BasicResponse<FriendRequestListResponse>>

    suspend fun getAllReceivedFriendRequests(): Flow<BasicResponse<FriendRequestListResponse>>
}