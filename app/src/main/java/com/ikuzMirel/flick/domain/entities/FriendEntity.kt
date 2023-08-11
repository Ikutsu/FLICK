package com.ikuzMirel.flick.domain.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ikuzMirel.flick.domain.model.Friend
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "friends")
data class FriendEntity(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "uid")
    val userId: String,
    @ColumnInfo(name = "name")
    val username: String,
    @ColumnInfo(name = "cid")
    val collectionId: String,
    @ColumnInfo(name = "friend_with")
    val friendWith: String,
)

fun FriendEntity.toFriend() = Friend(
    name = username,
    userId = userId,
    collectionId = collectionId,
    latestMessage = "Hellow UWU",
    notification = 0,
    status = 1
)