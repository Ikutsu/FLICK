package com.ikuzMirel.flick.ui.chat

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ikuzMirel.flick.data.remote.websocket.WebSocketApi
import com.ikuzMirel.flick.data.repositories.PreferencesRepository
import com.ikuzMirel.flick.data.requests.SendMessageRequest
import com.ikuzMirel.flick.data.room.dao.MessageDao
import com.ikuzMirel.flick.domain.entities.toMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val webSocketApi: WebSocketApi,
    private val preferencesRepository: PreferencesRepository,
    private val messageDao: MessageDao
) : ViewModel() {

    val state = mutableStateOf(ChatUIState())

    init {
        viewModelScope.launch {
            state.value = state.value.copy(
                senderId = preferencesRepository.getUserId()
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
            messageDao.getMessages(collectionId).collect { messages ->
                state.value = state.value.copy(
                    isLoading = false,
                    messages = messages.map {
                        it.toMessage()
                    }
                )
            }
        }
    }

    fun sendMessage(content: String, collectionId: String, receiverId: String) {
        viewModelScope.launch {
            if (state.value.message.isNotBlank()) {
                val request = SendMessageRequest(
                    content, collectionId, receiverId
                )
                val json = Json.encodeToString(request)
                webSocketApi.sendMessage(json)
                state.value = state.value.copy(message = "")
            }
        }
    }
}