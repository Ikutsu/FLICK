package com.ikuzMirel.flick.data.repositories

import com.ikuzMirel.flick.data.requests.MessageListRequest
import com.ikuzMirel.flick.data.response.BasicResponse
import com.ikuzMirel.flick.data.response.MessageListResponse
import kotlinx.coroutines.flow.Flow

interface ChatRepository {
    suspend fun getChatMassages(request: MessageListRequest): Flow<BasicResponse<MessageListResponse>>
}