package com.ikuzMirel.flick.data.repositories

import com.ikuzMirel.flick.data.dto.chat.request.MessageListRequestDto
import com.ikuzMirel.flick.data.dto.chat.response.MessageListResponseDto
import com.ikuzMirel.flick.data.remote.chat.ChatRemote
import com.ikuzMirel.flick.data.utils.ResponseResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ChatRepositoryImpl @Inject constructor(
    private val remote: ChatRemote
) : ChatRepository {
    override suspend fun getChatMassages(request: MessageListRequestDto): Flow<ResponseResult<MessageListResponseDto>> {
        return flow {
            when (val response = remote.getChatMessages(request)){
                is ResponseResult.Error -> {
                    emit(ResponseResult.error(response.errorMessage))
                }
                is ResponseResult.Success -> {
                    emit(ResponseResult.success(response.data))
                }
            }
        }
    }
}