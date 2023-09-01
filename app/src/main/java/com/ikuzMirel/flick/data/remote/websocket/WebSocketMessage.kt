package com.ikuzMirel.flick.data.remote.websocket

import com.ikuzMirel.flick.data.serializer.WebSocketMsgSerializer

@kotlinx.serialization.Serializable(with = WebSocketMsgSerializer::class)
data class WebSocketMessage(
    val type: String,
    val data: Any
)