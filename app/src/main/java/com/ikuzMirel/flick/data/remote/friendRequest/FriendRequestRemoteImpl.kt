package com.ikuzMirel.flick.data.remote.friendRequest

import com.ikuzMirel.flick.data.constants.ENDPOINT_FRIEND_REQUESTS_RECEIVED
import com.ikuzMirel.flick.data.constants.ENDPOINT_FRIEND_REQUESTS_SENT
import com.ikuzMirel.flick.data.constants.ENDPOINT_FRIEND_REQUEST_ACCEPT
import com.ikuzMirel.flick.data.constants.ENDPOINT_FRIEND_REQUEST_CANCEL
import com.ikuzMirel.flick.data.constants.ENDPOINT_FRIEND_REQUEST_REJECT
import com.ikuzMirel.flick.data.constants.ENDPOINT_FRIEND_REQUEST_SEND
import com.ikuzMirel.flick.data.constants.FRIEND_REQUEST_ERROR
import com.ikuzMirel.flick.data.requests.SendFriendReqRequest
import com.ikuzMirel.flick.data.response.BasicResponse
import com.ikuzMirel.flick.data.response.FriendRequestListResponse
import com.ikuzMirel.flick.data.response.processHttpResponse
import com.ikuzMirel.flick.domain.entities.FriendRequestEntity
import io.ktor.client.HttpClient
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
    override suspend fun cancelFriendRequest(requestId: String): BasicResponse<String> {
        return processHttpResponse(
            request = client.post(ENDPOINT_FRIEND_REQUEST_CANCEL) {
                parameter("id", requestId)
            },
            specificError = FRIEND_REQUEST_ERROR
        )
    }

    override suspend fun acceptFriendRequest(requestId: String): BasicResponse<String> {
        return processHttpResponse(
            request = client.post(ENDPOINT_FRIEND_REQUEST_ACCEPT) {
                parameter("id", requestId)
            },
            specificError = FRIEND_REQUEST_ERROR
        )
    }

    override suspend fun rejectFriendRequest(requestId: String): BasicResponse<String> {
        return processHttpResponse(
            request = client.post(ENDPOINT_FRIEND_REQUEST_REJECT) {
                parameter("id", requestId)
            },
            specificError = FRIEND_REQUEST_ERROR
        )
    }

    override suspend fun getAllSentFriendRequests(): BasicResponse<FriendRequestListResponse> {
        return processHttpResponse(
            request = client.get(ENDPOINT_FRIEND_REQUESTS_SENT),
            specificError = FRIEND_REQUEST_ERROR
        )
    }

    override suspend fun getAllReceivedFriendRequests(): BasicResponse<FriendRequestListResponse> {
        return processHttpResponse(
            request = client.get(ENDPOINT_FRIEND_REQUESTS_RECEIVED),
            specificError = FRIEND_REQUEST_ERROR
        )
    }
}