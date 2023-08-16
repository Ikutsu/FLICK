package com.ikuzMirel.flick.ui.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ikuzMirel.flick.data.remote.websocket.WebSocketApi
import com.ikuzMirel.flick.data.repositories.PreferencesRepository
import com.ikuzMirel.flick.data.requests.SendMessageRequest
import com.ikuzMirel.flick.data.response.BasicResponse
import com.ikuzMirel.flick.data.room.dao.MessageDao
import com.ikuzMirel.flick.domain.entities.toMessage
import com.ikuzMirel.flick.domain.model.Message
import com.ikuzMirel.flick.domain.model.MessageState
import com.ikuzMirel.flick.domain.model.toMessageEntity
import com.ikuzMirel.flick.utils.toDate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.bson.types.ObjectId
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
            messageDao.getMessages(collectionId).distinctUntilChanged().collect { messages ->
                _uiState.update {
                    it.copy(
                        messages = messages.sortedByDescending { it.timestamp }.map {
                            it.toMessage()
                        }
                    )
                }
            }
        }
    }

    fun sendMessage(content: String, collectionId: String, receiverId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            if (_uiState.value.message.isNotBlank()) {
                val request = SendMessageRequest(
                    content,
                    collectionId,
                    receiverId,
                    ObjectId().toString()
                )

                val timestamp = System.currentTimeMillis()
                val message = Message(
                    content,
                    _uiState.value.senderId,
                    timestamp.toDate(),
                    request.id,
                    MessageState.SENDING.name,
                    false
                )
                messageDao.upsertMessage(message.toMessageEntity(timestamp, collectionId))

                val json = Json.encodeToString(request)

                val list = uiState.value.messages.toMutableList()
                list.add(0, message)

                _uiState.update {
                    it.copy(
                        message = "",
                        messages = list.toList()
                    )
                }
                webSocketApi.sendMessage(json).let {
                    if (it is BasicResponse.Error){
                        messageDao.upsertMessage(message.toMessageEntity(timestamp, collectionId).copy(state = MessageState.ERROR.name))
                        val index = list.indexOf(message)
                        list[index] = message.copy(state = MessageState.ERROR.name)
                        _uiState.update {
                            it.copy(
                                message = "",
                                messages = list.toList()
                            )
                        }
                    }
                }
            }
        }
    }

    fun resendMessage(message: Message, collectionId: String, receiverId: String) {
        viewModelScope.launch(Dispatchers.IO) {

            val request = SendMessageRequest(
                message.content,
                collectionId,
                receiverId,
                message.id
            )

            val timestamp = System.currentTimeMillis()
            val json = Json.encodeToString(request)

            val newMessage = message.copy(
                state = MessageState.SENDING.name
            )

            val list = uiState.value.messages.toMutableList()
            val index = list.indexOf(message)
            list.removeAt(index)
            list.add(0, newMessage)

            _uiState.update {
                it.copy(
                    message = "",
                    messages = list.toList()
                )
            }

            webSocketApi.sendMessage(json).let {
                if (it is BasicResponse.Error){
                    messageDao.upsertMessage(message.toMessageEntity(timestamp, collectionId).copy(state = MessageState.ERROR.name))
                    val newIndex = list.indexOf(message)
                    list[newIndex] = message.copy(state = MessageState.ERROR.name)
                    _uiState.update {
                        it.copy(
                            message = "",
                            messages = list.toList()
                        )
                    }
                }
            }
        }
    }

    fun markMessageAsRead(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            messageDao.updateUnreadMessages(id)
        }
    }
}