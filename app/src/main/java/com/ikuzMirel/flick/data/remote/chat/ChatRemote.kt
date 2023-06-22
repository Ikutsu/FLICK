package com.ikuzMirel.flick.data.remote.chat

import com.ikuzMirel.flick.data.requests.MessageListRequest
import com.ikuzMirel.flick.data.response.BasicResponse
import com.ikuzMirel.flick.data.response.MessageListResponse

interface ChatRemote {
    suspend fun getChatMessages(request: MessageListRequest): BasicResponse<MessageListResponse>
}