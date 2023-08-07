package com.ikuzMirel.flick.data.remote.friendRequest

import com.ikuzMirel.flick.data.requests.FriendReqRequest
import com.ikuzMirel.flick.data.requests.SendFriendReqRequest
import com.ikuzMirel.flick.data.response.BasicResponse
import com.ikuzMirel.flick.data.response.FriendRequestListResponse
import com.ikuzMirel.flick.domain.entities.FriendRequestEntity

interface FriendRequestRemote {
    suspend fun sendFriendRequest(
        request: SendFriendReqRequest
    ): BasicResponse<FriendRequestEntity>

    suspend fun cancelFriendRequest(
        request: FriendReqRequest
    ): BasicResponse<String>

    suspend fun acceptFriendRequest(
        request: FriendReqRequest
    ): BasicResponse<String>

    suspend fun rejectFriendRequest(
        request: FriendReqRequest
    ): BasicResponse<String>

    suspend fun getAllSentFriendRequests(
        token: String
    ): BasicResponse<FriendRequestListResponse>

    suspend fun getAllReceivedFriendRequests(
        token: String
    ): BasicResponse<FriendRequestListResponse>
}