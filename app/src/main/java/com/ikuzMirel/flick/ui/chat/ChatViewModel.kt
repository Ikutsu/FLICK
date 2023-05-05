package com.ikuzMirel.flick.ui.chat

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ikuzMirel.flick.data.dto.chat.request.MessageListRequestDto
import com.ikuzMirel.flick.data.dto.chat.request.SendMessageDto
import com.ikuzMirel.flick.data.repositories.PreferencesRepository
import com.ikuzMirel.flick.data.remote.websocket.WebSocketService
import com.ikuzMirel.flick.data.repositories.ChatRepository
import com.ikuzMirel.flick.data.utils.ResponseResult
import com.ikuzMirel.flick.domain.model.Message
import com.ikuzMirel.flick.utils.toDate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val chatRepository: ChatRepository,
    private val webSocketService: WebSocketService,
    private val preferencesRepository: PreferencesRepository
) : ViewModel() {

    val state = mutableStateOf(ChatUIState())

    init {
        viewModelScope.launch {
            state.value = state.value.copy(
                senderId = preferencesRepository.getUserId().data!!
            )
        }
    }

    fun onEvent(event: ChatUIEvent) {
        when (event) {
            is ChatUIEvent.MassageChanged -> state.value = state.value.copy(message = event.message)
        }
    }

    fun getChatMessages(collectionId: String) {
        viewModelScope.launch {
            state.value = state.value.copy(isLoading = true)
            val token = preferencesRepository.getJwt().data!!
            val request = MessageListRequestDto(
                collectionId = collectionId,
                token = token
            )
            chatRepository.getChatMassages(request).collect { response ->
                when (response) {
                    is ResponseResult.Error -> {
                        state.value = state.value.copy(isLoading = false)
                    }
                    is ResponseResult.Success -> {
                        if (response.data == null) {
                            state.value = state.value.copy(isLoading = false)
                        } else {
                            state.value = state.value.copy(
                                isLoading = false
                            )
                            state.value = state.value.copy(
                                messages = response.data.data!!.messages.map {
                                    Message(
                                        content = it.content,
                                        user = it.senderUid,
                                        time = it.timestamp.toDate(),
                                    )
                                }
                            )
                        }
                    }
                }
            }
        }
    }

    fun sendMessage(content: String, collectionId: String, receiverId: String){
        viewModelScope.launch {
            if (state.value.message.isNotBlank()){
                val request = SendMessageDto(
                    content, collectionId, receiverId
                )
                val json = Json.encodeToString(request)
                webSocketService.sendMessage(json)
                state.value = state.value.copy(message = "")
            }
        }
    }

    fun receiveMessage(collectionId: String){
        when (webSocketService.checkConnection()) {
            false -> {}
            true -> {
                webSocketService.receiveMessage().onEach { message ->
                    if (message.cid == collectionId) {
                        val messageList = state.value.messages.toMutableList().apply {
                            add(0, Message(message.content, message.senderUid, message.timestamp.toDate()))
                        }
                        state.value = state.value.copy(messages = messageList)
                    }
                }.launchIn(viewModelScope)
            }
        }
    }
}