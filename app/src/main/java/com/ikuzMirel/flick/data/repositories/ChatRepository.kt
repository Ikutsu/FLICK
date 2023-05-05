package com.ikuzMirel.flick.data.repositories

import com.ikuzMirel.flick.data.dto.chat.request.MessageListRequestDto
import com.ikuzMirel.flick.data.dto.chat.response.MessageListResponseDto
import com.ikuzMirel.flick.data.utils.ResponseResult
import kotlinx.coroutines.flow.Flow

interface ChatRepository {
    suspend fun getChatMassages(request: MessageListRequestDto): Flow<ResponseResult<MessageListResponseDto>>
}