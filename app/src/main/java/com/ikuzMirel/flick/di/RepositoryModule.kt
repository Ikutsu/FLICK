package com.ikuzMirel.flick.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.ikuzMirel.flick.data.remote.auth.AuthRemote
import com.ikuzMirel.flick.data.remote.chat.ChatRemote
import com.ikuzMirel.flick.data.remote.friendRequest.FriendRequestRemote
import com.ikuzMirel.flick.data.remote.user.UserRemote
import com.ikuzMirel.flick.data.repositories.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun providePreferencesRepository(
        dataStore: DataStore<Preferences>
    ): PreferencesRepository = PreferencesRepository(dataStore)

    @Provides
    @Singleton
    fun provideAuthRepository(
        authRemote: AuthRemote,
        preferencesRepository: PreferencesRepository,
        @ApplicationContext context: Context,
        httpClient: HttpClient
    ): AuthRepository = AuthRepositoryImpl(httpClient, authRemote, preferencesRepository, context)

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

    @Provides
    @Singleton
    fun provideFriendReqRepository(
        friendRequestRemote: FriendRequestRemote
    ): FriendReqRepository = FriendReqRepositoryImpl(friendRequestRemote)
}