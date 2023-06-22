package com.ikuzMirel.flick.data.remote.user

import com.ikuzMirel.flick.data.model.UserData
import com.ikuzMirel.flick.data.requests.FriendListRequest
import com.ikuzMirel.flick.data.requests.UserDataRequest
import com.ikuzMirel.flick.data.response.BasicResponse
import com.ikuzMirel.flick.data.response.FriendListResponse

interface UserRemote {
    suspend fun getUserInfo(
        request: UserDataRequest
    ): BasicResponse<UserData>

    suspend fun getUserFriends(
        request: FriendListRequest
    ): BasicResponse<FriendListResponse>
}