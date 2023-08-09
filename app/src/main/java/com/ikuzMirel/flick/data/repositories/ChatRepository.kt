package com.ikuzMirel.flick.data.repositories

import com.ikuzMirel.flick.data.response.BasicResponse
import com.ikuzMirel.flick.data.response.MessageListResponse
import kotlinx.coroutines.flow.Flow

interface ChatRepository {
    suspend fun getChatMassages(collectionId: String): Flow<BasicResponse<MessageListResponse>>
}