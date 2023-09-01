package com.ikuzMirel.flick.data.room.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.ikuzMirel.flick.domain.entities.FriendEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FriendDao {
    @Upsert
    fun upsertFriend(friend: FriendEntity)
    @Query("SELECT * FROM friends WHERE friend_with = :userId")
    fun getAllFriends(userId: String): Flow<List<FriendEntity>>

    @Query("SELECT * FROM friends WHERE uid = :friendUid And friend_with = :userId")
    fun getFriend(friendUid: String, userId: String): Flow<FriendEntity>

    @Query("SELECT cid FROM friends WHERE friend_with = :userId")
    fun getAllFriendsCIDs(userId: String): Flow<List<String>>

    @Query("SELECT * FROM friends WHERE cid = :cid")
    fun getFriendWithCID(cid: String): Flow<FriendEntity>

    @Query("SELECT cid FROM friends WHERE uid = :friendUid")
    fun getCidWithUserId(friendUid: String): String

    @Query("UPDATE friends SET unread_count = :unreadCount WHERE uid = :friendUid")
    fun updateUnreadCount(friendUid: String, unreadCount: Int)

    @Query("SELECT unread_count FROM friends WHERE uid = :friendUid")
    fun getLateReadMessageTime(friendUid: String): Long
    @Query("UPDATE friends SET last_read_message_time = :lastReadMessageTime WHERE uid = :friendUid")
    fun updateLastReadMessageTime(friendUid: String, lastReadMessageTime: Long)

    @Query("UPDATE friends SET latest_message = :latestMessage WHERE uid = :friendUid")
    fun updateLatestMessage(friendUid: String, latestMessage: String)
}