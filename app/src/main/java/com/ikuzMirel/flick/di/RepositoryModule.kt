package com.ikuzMirel.flick.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.ikuzMirel.flick.data.remote.auth.AuthRemote
import com.ikuzMirel.flick.data.remote.chat.ChatRemote
import com.ikuzMirel.flick.data.remote.user.UserRemote
import com.ikuzMirel.flick.data.remote.websocket.WebSocketService
import com.ikuzMirel.flick.data.repositories.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun providePreferencesRepository(
        dataStore: DataStore<Preferences>
    ): PreferencesRepository = PreferencesRepositoryImpl(dataStore)

    @Provides
    @Singleton
    fun provideAuthRepository(
        authRemote: AuthRemote,
        preferencesRepository: PreferencesRepository,
        webSocketService: WebSocketService
    ): AuthRepository = AuthRepositoryImpl(authRemote, preferencesRepository, webSocketService)

    @Provides
    @Singleton
    fun provideUserRepository(
        userRemote: UserRemote
    ): UserRepository = UserRepositoryImpl(userRemote)

    @Provides
    @Singleton
    fun provideChatRepository(
        chatRemote: ChatRemote
    ): ChatRepository = ChatRepositoryImpl(chatRemote)
}