package com.ikuzMirel.flick.domain.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ikuzMirel.flick.domain.model.Message
import com.ikuzMirel.flick.utils.toDate
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "messages")
data class MessageEntity(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id")
    val id: String,
    @ColumnInfo(name = "content")
    val content: String,
    @ColumnInfo(name = "sender_uid")
    val senderUid: String,
    @ColumnInfo(name = "timestamp")
    val timestamp: Long,
    @ColumnInfo(name = "cid")
    val collectionId: String,
    @ColumnInfo(name = "state")
    val state: String
)

fun MessageEntity.toMessage() = Message(
    content = content,
    userId = senderUid,
    timestamp = timestamp.toDate(),
    id = id,
    state = state
)