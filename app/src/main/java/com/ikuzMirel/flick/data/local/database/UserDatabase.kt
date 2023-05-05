package com.ikuzMirel.flick.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ikuzMirel.flick.data.local.dao.FriendDao
import com.ikuzMirel.flick.data.local.dao.MessageDao
import com.ikuzMirel.flick.domain.entities.FriendEntity
import com.ikuzMirel.flick.domain.entities.MessageEntity

@Database(entities = [MessageEntity::class, FriendEntity::class], version = 1)
abstract class UserDatabase: RoomDatabase() {
    abstract fun messageDao(): MessageDao
    abstract fun friendDao(): FriendDao
}