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

    @Query("SELECT * FROM friends")
    fun getAllFriends(): Flow<List<FriendEntity>>

    @Query("SELECT * FROM friends WHERE uid = :userId")
    fun getFriend(userId: String): Flow<FriendEntity>

    @Query("SELECT cid FROM friends")
    fun getAllFriendsCIDs(): Flow<List<String>>
}