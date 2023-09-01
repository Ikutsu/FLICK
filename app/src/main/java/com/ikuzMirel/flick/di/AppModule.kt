package com.ikuzMirel.flick.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.work.WorkManager
import com.ikuzMirel.flick.data.remote.auth.AuthRemote
import com.ikuzMirel.flick.data.remote.auth.AuthRemoteImpl
import com.ikuzMirel.flick.data.remote.chat.ChatRemote
import com.ikuzMirel.flick.data.remote.chat.ChatRemoteImpl
import com.ikuzMirel.flick.data.remote.friendRequest.FriendRequestRemote
import com.ikuzMirel.flick.data.remote.friendRequest.FriendRequestRemoteImpl
import com.ikuzMirel.flick.data.remote.user.UserRemote
import com.ikuzMirel.flick.data.remote.user.UserRemoteImpl
import com.ikuzMirel.flick.data.remote.websocket.WebSocketApi
import com.ikuzMirel.flick.data.remote.websocket.WebSocketApiImpl
import com.ikuzMirel.flick.data.repositories.*
import com.ikuzMirel.flick.data.room.dao.FriendDao
import com.ikuzMirel.flick.handler.LastMessageQueueHandler
import com.ikuzMirel.flick.service.NotificationService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.plugins.observer.*
import io.ktor.client.plugins.websocket.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "Preferences")

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun providePreferenceDataStore(
        @ApplicationContext context: Context
    ): DataStore<Preferences> = context.dataStore

    @Provides
    @Singleton
    fun provideAuthRemote(
        httpClient: HttpClient
    ): AuthRemote = AuthRemoteImpl(httpClient)

    @Provides
    @Singleton
    fun provideUserRemote(
        httpClient: HttpClient
    ): UserRemote = UserRemoteImpl(httpClient)

    @Provides
    @Singleton
    fun provideChatRemote(
        httpClient: HttpClient
    ): ChatRemote = ChatRemoteImpl(httpClient)

    @Provides
    @Singleton
    fun provideFriendRequestRemote(
        httpClient: HttpClient
    ): FriendRequestRemote = FriendRequestRemoteImpl(httpClient)

    @Provides
    @Singleton
    fun provideWebSocketService(
        httpClient: HttpClient
    ): WebSocketApi = WebSocketApiImpl(httpClient)

    @Provides
    @Singleton
    fun provideNotificationService(
        @ApplicationContext context: Context,
        friendDao: FriendDao,
        preferencesRepository: PreferencesRepository
    ): NotificationService = NotificationService(context, friendDao, preferencesRepository)

    @Provides
    @Singleton
    fun provideWorkManager(
        @ApplicationContext context: Context
    ): WorkManager = WorkManager.getInstance(context)

    @Provides
    @Singleton
    fun provideLastMessageQueueHandler(
        friendDao: FriendDao
    ): LastMessageQueueHandler = LastMessageQueueHandler(friendDao)
}