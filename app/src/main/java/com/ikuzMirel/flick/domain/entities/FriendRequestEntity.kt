package com.ikuzMirel.flick.domain.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

enum class FriendRequestStatus {
    PENDING,
    ACCEPTED,
    REJECTED,
    CANCELLED
}

@Serializable
@Entity(tableName = "friend_requests")
data class FriendRequestEntity(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id")
    val id: String,
    @ColumnInfo(name = "sender_id")
    val senderId: String,
    @ColumnInfo(name = "sender_name")
    val senderName: String,
    @ColumnInfo(name = "receiver_id")
    val receiverId: String,
    @ColumnInfo(name = "receiver_name")
    val receiverName: String,
    @ColumnInfo(name = "status")
    val status: String,
)
