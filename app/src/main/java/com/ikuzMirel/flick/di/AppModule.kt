package com.ikuzMirel.flick.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.ikuzMirel.flick.BuildConfig
import com.ikuzMirel.flick.data.auth.AuthApi
import com.ikuzMirel.flick.data.auth.AuthRepository
import com.ikuzMirel.flick.data.auth.AuthRepositoryImpl
import com.ikuzMirel.flick.data.user.UserPreferencesRepository
import com.ikuzMirel.flick.data.user.UserPreferencesRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "Preferences")
private val baseUrl = BuildConfig.ServerUrl

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideAuthApi(): AuthApi {
        return Retrofit.Builder()
            .baseUrl(baseUrl)//TODO: Local
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create()
    }

    @Provides
    @Singleton
    fun providePreferenceDataStore(
        @ApplicationContext context: Context
    ): DataStore<Preferences> {
        return context.dataStore
    }

    @Provides
    @Singleton
    fun provideUserPreferencesRepository(
        dataStore: DataStore<Preferences>,
        authApi: AuthApi
    ): UserPreferencesRepository {
        return UserPreferencesRepositoryImpl(dataStore, authApi)
    }

    @Provides
    @Singleton
    fun provideAuthRepository(
        authApi: AuthApi,
        userPreferencesRepository: UserPreferencesRepository
    ): AuthRepository {
        return AuthRepositoryImpl(authApi, userPreferencesRepository)
    }
}