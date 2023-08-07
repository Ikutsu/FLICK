package com.ikuzMirel.flick.di

import android.content.Context
import androidx.room.Room
import com.ikuzMirel.flick.data.room.dao.FriendDao
import com.ikuzMirel.flick.data.room.dao.FriendReqDao
import com.ikuzMirel.flick.data.room.dao.MessageDao
import com.ikuzMirel.flick.data.room.database.UserDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {

    @Provides
    @Singleton
    fun provideRoomDatabase(
        @ApplicationContext context: Context
    ): UserDatabase = Room.databaseBuilder(
        context,
        UserDatabase::class.java,
        "user_database"
    ).build()

    @Provides
    @Singleton
    fun provideMessageDao(
        roomDatabase: UserDatabase
    ): MessageDao = roomDatabase.messageDao()

    @Provides
    @Singleton
    fun provideFriendDao(
        roomDatabase: UserDatabase
    ): FriendDao = roomDatabase.friendDao()

    @Provides
    @Singleton
    fun provideFriendReqDao(
        roomDatabase: UserDatabase
    ): FriendReqDao = roomDatabase.friendReqDao()
}