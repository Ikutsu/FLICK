package com.ikuzMirel.flick.data.remote.user

import com.ikuzMirel.flick.data.constants.ENDPOINT_USER_FRIEND
import com.ikuzMirel.flick.data.constants.ENDPOINT_USER_FRIENDS
import com.ikuzMirel.flick.data.constants.ENDPOINT_USER_INFO
import com.ikuzMirel.flick.data.constants.ENDPOINT_USER_SEARCH
import com.ikuzMirel.flick.data.constants.FRIEND_NOT_EXIST
import com.ikuzMirel.flick.data.constants.USER_NOT_EXIST
import com.ikuzMirel.flick.data.model.UserData
import com.ikuzMirel.flick.data.response.BasicResponse
import com.ikuzMirel.flick.data.response.FriendListResponse
import com.ikuzMirel.flick.data.response.UserListResponse
import com.ikuzMirel.flick.data.response.processHttpResponse
import com.ikuzMirel.flick.domain.entities.FriendEntity
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import javax.inject.Inject

class UserRemoteImpl @Inject constructor(
    private val client: HttpClient
) : UserRemote {
    override suspend fun getUserInfo(userId: String): BasicResponse<UserData> {
        return processHttpResponse(
            request = client.get(ENDPOINT_USER_INFO) {
                parameter("id", userId)
            },
            specificError = USER_NOT_EXIST
        )
    }

    override suspend fun getUserFriends(): BasicResponse<FriendListResponse> {
        return processHttpResponse(
            request = client.get(ENDPOINT_USER_FRIENDS),
            specificError = USER_NOT_EXIST
        )
    }


    override suspend fun getUserFriend(
        friendUserId: String
    ): BasicResponse<FriendEntity> {
        return processHttpResponse(
            request = client.get(ENDPOINT_USER_FRIEND) {
                parameter("friendId", friendUserId)
            },
            specificError = FRIEND_NOT_EXIST
        )
    }

    override suspend fun searchUsers(searchQuery: String): BasicResponse<UserListResponse> {
        return processHttpResponse(
            request = client.get(ENDPOINT_USER_SEARCH) {
                parameter("username", searchQuery)
            },
            specificError = USER_NOT_EXIST
        )
    }
}