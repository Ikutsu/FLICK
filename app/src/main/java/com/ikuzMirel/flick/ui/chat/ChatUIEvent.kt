package com.ikuzMirel.flick.ui.chat

sealed class ChatUIEvent{
    data class MassageChanged(val message: String) : ChatUIEvent()
}
