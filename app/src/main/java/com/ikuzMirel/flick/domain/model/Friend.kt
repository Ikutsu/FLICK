package com.ikuzMirel.flick.domain.model

data class Friend(
//    val avatar: ,
    val status: Int,
    val name: String,
    val userId: String,
    val collectionId: String,
    val latestMessage: String,
    val unreadCount: Int
)