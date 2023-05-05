package com.ikuzMirel.flick.data.remote.chat

import com.ikuzMirel.flick.data.dto.chat.request.MessageListRequestDto
import com.ikuzMirel.flick.data.dto.chat.response.MessageListResponseDto
import com.ikuzMirel.flick.data.utils.ResponseResult

interface ChatRemote {
    suspend fun getChatMessages(request: MessageListRequestDto): ResponseResult<MessageListResponseDto>
}