package com.ikuzMirel.flick.data.remote.user

import com.ikuzMirel.flick.data.constants.ENDPOINT_USER_FRIEND
import com.ikuzMirel.flick.data.constants.ENDPOINT_USER_FRIENDS
import com.ikuzMirel.flick.data.constants.ENDPOINT_USER_INFO
import com.ikuzMirel.flick.data.constants.ENDPOINT_USER_SEARCH
import com.ikuzMirel.flick.data.constants.FRIEND_NOT_EXIST
import com.ikuzMirel.flick.data.constants.USER_NOT_EXIST
import com.ikuzMirel.flick.data.model.UserData
import com.ikuzMirel.flick.data.requests.FriendListRequest
import com.ikuzMirel.flick.data.requests.SearchUsersRequest
import com.ikuzMirel.flick.data.requests.UserDataRequest
import com.ikuzMirel.flick.data.response.BasicResponse
import com.ikuzMirel.flick.data.response.FriendListResponse
import com.ikuzMirel.flick.data.response.UserListResponse
import com.ikuzMirel.flick.data.response.processHttpResponse
import com.ikuzMirel.flick.domain.entities.FriendEntity
import io.ktor.client.HttpClient
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import javax.inject.Inject

class UserRemoteImpl @Inject constructor(
    private val client: HttpClient
) : UserRemote {
    override suspend fun getUserInfo(request: UserDataRequest): BasicResponse<UserData> {
        return processHttpResponse(
            request = client.get(ENDPOINT_USER_INFO) {
                bearerAuth(request.token)
            },
            specificError = USER_NOT_EXIST
        )
    }

    override suspend fun getUserFriends(request: FriendListRequest): BasicResponse<FriendListResponse> {
        return processHttpResponse(
            request = client.get(ENDPOINT_USER_FRIENDS) {
                parameter("id", request.id)
                bearerAuth(request.token)
            },
            specificError = USER_NOT_EXIST
        )
    }


    override suspend fun getUserFriend(
        userId: String,
        friendUserId: String,
        token: String
    ): BasicResponse<FriendEntity> {
        return processHttpResponse(
            request = client.get(ENDPOINT_USER_FRIEND) {
                parameter("id", userId)
                parameter("friendId", friendUserId)
                bearerAuth(token)
            },
            specificError = FRIEND_NOT_EXIST
        )
    }

    override suspend fun searchUsers(request: SearchUsersRequest): BasicResponse<UserListResponse> {
        return processHttpResponse(
            request = client.get(ENDPOINT_USER_SEARCH) {
                parameter("username", request.searchQuery)
                bearerAuth(request.token)
            },
            specificError = USER_NOT_EXIST
        )
    }
}