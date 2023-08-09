package com.ikuzMirel.flick.data.remote.chat

import com.ikuzMirel.flick.data.response.BasicResponse
import com.ikuzMirel.flick.data.response.MessageListResponse

interface ChatRemote {
    suspend fun getChatMessages(collectionId: String): BasicResponse<MessageListResponse>
}