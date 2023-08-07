package com.ikuzMirel.flick.data.room.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.ikuzMirel.flick.domain.entities.FriendRequestEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FriendReqDao {
    @Upsert
    fun upsertFriendReq(friendReq: FriendRequestEntity)

    @Query("SELECT * FROM friend_requests")
    fun getAllFriendReqs(): Flow<List<FriendRequestEntity>>

    @Query("SELECT * FROM friend_requests WHERE id = :id")
    fun getFriendReq(id: String): FriendRequestEntity

    @Query("DELETE FROM friend_requests WHERE id = :id")
    fun deleteFriendReq(id: String)
}