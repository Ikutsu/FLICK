package com.ikuzMirel.flick.data.repositories

import com.ikuzMirel.flick.data.remote.chat.ChatRemote
import com.ikuzMirel.flick.data.response.BasicResponse
import com.ikuzMirel.flick.data.response.MessageListResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ChatRepositoryImpl @Inject constructor(
    private val remote: ChatRemote
) : ChatRepository {
    override suspend fun getChatMassages(collectionId: String): Flow<BasicResponse<MessageListResponse>> {
        return flow {
            when (val response = remote.getChatMessages(collectionId)){
                is BasicResponse.Error -> {
                    emit(BasicResponse.Error(response.errorMessage))
                }
                is BasicResponse.Success -> {
                    emit(BasicResponse.Success(response.data))
                }
            }
        }
    }
}