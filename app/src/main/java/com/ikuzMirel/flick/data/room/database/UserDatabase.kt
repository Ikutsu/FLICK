package com.ikuzMirel.flick.data.room.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ikuzMirel.flick.data.room.dao.FriendDao
import com.ikuzMirel.flick.data.room.dao.MessageDao
import com.ikuzMirel.flick.domain.entities.FriendEntity
import com.ikuzMirel.flick.domain.entities.MessageEntity

@Database(entities = [MessageEntity::class, FriendEntity::class], version = 1, exportSchema = false)
abstract class UserDatabase: RoomDatabase() {
    abstract fun messageDao(): MessageDao
    abstract fun friendDao(): FriendDao
}