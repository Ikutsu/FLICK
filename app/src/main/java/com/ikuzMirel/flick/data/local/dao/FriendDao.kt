package com.ikuzMirel.flick.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.ikuzMirel.flick.domain.entities.FriendEntity

@Dao
interface FriendDao {
    @Upsert
    fun upsertFriend(friend: FriendEntity)

    @Query("SELECT * FROM friends")
    fun getAllFriends(): List<FriendEntity>
}