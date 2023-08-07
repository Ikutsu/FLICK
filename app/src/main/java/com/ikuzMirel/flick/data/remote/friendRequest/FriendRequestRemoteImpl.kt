package com.ikuzMirel.flick.data.remote.friendRequest

import com.ikuzMirel.flick.data.constants.ENDPOINT_FRIEND_REQUESTS_RECEIVED
import com.ikuzMirel.flick.data.constants.ENDPOINT_FRIEND_REQUESTS_SENT
import com.ikuzMirel.flick.data.constants.ENDPOINT_FRIEND_REQUEST_ACCEPT
import com.ikuzMirel.flick.data.constants.ENDPOINT_FRIEND_REQUEST_CANCEL
import com.ikuzMirel.flick.data.constants.ENDPOINT_FRIEND_REQUEST_REJECT
import com.ikuzMirel.flick.data.constants.ENDPOINT_FRIEND_REQUEST_SEND
import com.ikuzMirel.flick.data.constants.FRIEND_REQUEST_ERROR
import com.ikuzMirel.flick.data.requests.FriendReqRequest
import com.ikuzMirel.flick.data.requests.SendFriendReqRequest
import com.ikuzMirel.flick.data.response.BasicResponse
import com.ikuzMirel.flick.data.response.FriendRequestListResponse
import com.ikuzMirel.flick.data.response.processHttpResponse
import com.ikuzMirel.flick.domain.entities.FriendRequestEntity
import io.ktor.client.HttpClient
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import javax.inject.Inject

class FriendRequestRemoteImpl @Inject constructor(
    private val client: HttpClient
) : FriendRequestRemote {
    override suspend fun sendFriendRequest(request: SendFriendReqRequest): BasicResponse<FriendRequestEntity> {
        return processHttpResponse(
            request = client.post(ENDPOINT_FRIEND_REQUEST_SEND) {
                setBody(request)
            },
            specificError = FRIEND_REQUEST_ERROR
        )
    }
    override suspend fun cancelFriendRequest(request: FriendReqRequest): BasicResponse<String> {
        return processHttpResponse(
            request = client.post(ENDPOINT_FRIEND_REQUEST_CANCEL) {
                parameter("id", request.requestId)
            },
            specificError = FRIEND_REQUEST_ERROR
        )
    }

    override suspend fun acceptFriendRequest(request: FriendReqRequest): BasicResponse<String> {
        return processHttpResponse(
            request = client.post(ENDPOINT_FRIEND_REQUEST_ACCEPT) {
                parameter("id", request.requestId)
            },
            specificError = FRIEND_REQUEST_ERROR
        )
    }

    override suspend fun rejectFriendRequest(request: FriendReqRequest): BasicResponse<String> {
        return processHttpResponse(
            request = client.post(ENDPOINT_FRIEND_REQUEST_REJECT) {
                parameter("id", request.requestId)
            },
            specificError = FRIEND_REQUEST_ERROR
        )
    }

    override suspend fun getAllSentFriendRequests(token: String): BasicResponse<FriendRequestListResponse> {
        return processHttpResponse(
            request = client.get(ENDPOINT_FRIEND_REQUESTS_SENT) {
                bearerAuth(token)
            },
            specificError = FRIEND_REQUEST_ERROR
        )
    }

    override suspend fun getAllReceivedFriendRequests(token: String): BasicResponse<FriendRequestListResponse> {
        return processHttpResponse(
            request = client.get(ENDPOINT_FRIEND_REQUESTS_RECEIVED) {
                bearerAuth(token)
            },
            specificError = FRIEND_REQUEST_ERROR
        )
    }
}