package com.ikuzMirel.flick.ui.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ikuzMirel.flick.data.remote.websocket.WebSocketApi
import com.ikuzMirel.flick.data.repositories.PreferencesRepository
import com.ikuzMirel.flick.data.requests.SendMessageRequest
import com.ikuzMirel.flick.data.room.dao.MessageDao
import com.ikuzMirel.flick.domain.entities.toMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
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

    private val _uiState: MutableStateFlow<ChatUIState> = MutableStateFlow(ChatUIState())
    val uiState: StateFlow<ChatUIState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            _uiState.update {
                it.copy(senderId = preferencesRepository.getValue(PreferencesRepository.USERID)!!)
            }
        }
    }

    fun onMessageChange(message: String) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(message = message)
            }
        }
    }

    fun getChatMessages(collectionId: String) {
        viewModelScope.launch {
            messageDao.getMessages(collectionId).collect { messages ->
                _uiState.update {
                    it.copy(
                        messages = messages.map {
                            it.toMessage()
                        }
                    )
                }
            }
        }
    }

    fun sendMessage(content: String, collectionId: String, receiverId: String) {
        viewModelScope.launch {
            if (_uiState.value.message.isNotBlank()) {
                val request = SendMessageRequest(
                    content, collectionId, receiverId
                )
                val json = Json.encodeToString(request)
                webSocketApi.sendMessage(json)
                _uiState.update {
                    it.copy(message = "")
                }
            }
        }
    }
}