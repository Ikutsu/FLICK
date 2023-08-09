package com.ikuzMirel.flick.data.remote.friendRequest

import com.ikuzMirel.flick.data.requests.SendFriendReqRequest
import com.ikuzMirel.flick.data.response.BasicResponse
import com.ikuzMirel.flick.data.response.FriendRequestListResponse
import com.ikuzMirel.flick.domain.entities.FriendRequestEntity

interface FriendRequestRemote {
    suspend fun sendFriendRequest(
        request: SendFriendReqRequest
    ): BasicResponse<FriendRequestEntity>

    suspend fun cancelFriendRequest(
        requestId: String
    ): BasicResponse<String>

    suspend fun acceptFriendRequest(
        requestId: String
    ): BasicResponse<String>

    suspend fun rejectFriendRequest(
        requestId: String
    ): BasicResponse<String>

    suspend fun getAllSentFriendRequests(): BasicResponse<FriendRequestListResponse>

    suspend fun getAllReceivedFriendRequests(): BasicResponse<FriendRequestListResponse>
}