package com.ikuzMirel.flick.ui.chat

import com.ikuzMirel.flick.domain.model.Message

data class ChatUIState(
    val isLoading: Boolean = false,
    val message: String = "",
    val messages: List<Message> = emptyList(),
    val senderId: String = "",
    val receiverName: String = "",
    val receiverId: String = "",
    val collectionId: String = "",
)