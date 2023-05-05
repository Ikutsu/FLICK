package com.ikuzMirel.flick.domain.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "messages")
data class MessageEntity(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id")
    val id: String,
    @ColumnInfo(name = "content")
    val content: String,
    @ColumnInfo(name = "sender_uid")
    val senderUid: String,
    @ColumnInfo(name = "sender_name")
    val senderUsername: String,
    @ColumnInfo(name = "timestamp")
    val timestamp: Long,
    @ColumnInfo(name = "cid")
    val collectionId: String
)
