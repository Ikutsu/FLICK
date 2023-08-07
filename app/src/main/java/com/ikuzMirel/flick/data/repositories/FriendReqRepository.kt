package com.ikuzMirel.flick.data.repositories

import com.ikuzMirel.flick.data.requests.FriendReqRequest
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
        request: FriendReqRequest
    ): Flow<BasicResponse<FriendRequestEntity>>

    suspend fun acceptFriendRequest(
        request: FriendReqRequest
    ): Flow<BasicResponse<FriendRequestEntity>>

    suspend fun rejectFriendRequest(
        request: FriendReqRequest
    ): Flow<BasicResponse<FriendRequestEntity>>

    suspend fun getAllSentFriendRequests(
        token: String
    ): Flow<BasicResponse<FriendRequestListResponse>>

    suspend fun getAllReceivedFriendRequests(
        token: String
    ): Flow<BasicResponse<FriendRequestListResponse>>
}