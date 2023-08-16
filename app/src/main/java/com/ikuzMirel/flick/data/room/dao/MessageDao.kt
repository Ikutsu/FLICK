package com.ikuzMirel.flick.data.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import com.ikuzMirel.flick.domain.entities.MessageEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MessageDao {
    @Upsert
    fun upsertMessage(message: MessageEntity)

    @Insert
    fun insertMessage(message: MessageEntity)

    @Update
    fun updateMessage(message: MessageEntity)

    @Query("SELECT * FROM messages WHERE cid = :cid ")
    fun getMessages(cid: String): Flow<List<MessageEntity>>

    @Query("SELECT * FROM messages WHERE cid = :cid AND unread = 1")
    fun getUnreadMessages(cid: String): Flow<List<MessageEntity>>

    @Query("UPDATE messages SET unread = 0 WHERE id = :id")
    fun updateUnreadMessages(id: String)

    @Query("SELECT * FROM messages WHERE cid = :cid ORDER BY timestamp DESC LIMIT 1")
    fun getLatestMessage(cid: String): MessageEntity?
}