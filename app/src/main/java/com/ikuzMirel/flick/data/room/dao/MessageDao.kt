package com.ikuzMirel.flick.data.room.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.ikuzMirel.flick.domain.entities.MessageEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MessageDao {
    @Upsert
    fun upsertMessage(message: MessageEntity)

    @Query("SELECT * FROM messages WHERE cid = :cid ")
    fun getMessages(cid: String): Flow<List<MessageEntity>>
}