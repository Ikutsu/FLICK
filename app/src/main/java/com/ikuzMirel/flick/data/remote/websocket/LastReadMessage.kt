package com.ikuzMirel.flick.data.remote.websocket

import kotlinx.serialization.Serializable

@Serializable
data class LastReadMessage(
    val friendUserId: String,
    var lastReadMessageTime: Long
)

@Serializable
data class LastReadMessageSet(
    val lastReadMessageList: List<LastReadMessage>
)