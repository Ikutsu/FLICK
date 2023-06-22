package com.ikuzMirel.flick.data.response

import com.ikuzMirel.flick.data.serializer.WebSocketMsgSerializer

@kotlinx.serialization.Serializable(with = WebSocketMsgSerializer::class)
data class WebSocketResponse(
    val type: String,
    val data: Any
)