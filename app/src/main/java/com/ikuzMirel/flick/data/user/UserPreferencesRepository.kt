package com.ikuzMirel.flick.data.user

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.ikuzMirel.flick.data.auth.AuthApi
import com.ikuzMirel.flick.data.auth.AuthRepositoryImpl
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

interface UserPreferencesRepository {
    suspend fun getJwt(): String
    suspend fun setJwt(token: String)
    suspend fun getUsername(): String
    suspend fun setUsername(newUsername: String?)
    suspend fun getUserId(): String
    suspend fun setUserId(newUserId: String)
    suspend fun getEmail(): String
    suspend fun setEmail(newEmail: String?)
    suspend fun clear()
}

//TODO: implement network status observer
class UserPreferencesRepositoryImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>,
    private val api: AuthApi,
) : UserPreferencesRepository {

    companion object {
        private val jwt = stringPreferencesKey("JWT")
        private val username = stringPreferencesKey("username")
        private val userId = stringPreferencesKey("userId")
        private val email = stringPreferencesKey("email")
    }


    override suspend fun getJwt(): String {
        return dataStore.data.map {
            it[jwt] ?: ""
        }.first()
    }

    override suspend fun setJwt(token: String) {
        dataStore.edit { preferences ->
            preferences[jwt] = token
        }
    }

    override suspend fun getUsername(): String {
        return dataStore.data.map {
            it[username] ?: ""
        }.first()
    }

    override suspend fun setUsername(newUsername: String?) {
        if (newUsername != null) {
            dataStore.edit { preference ->
                preference[username] = newUsername
            }
        } else {
            val id = getUserId()
            val response = api.getUserInfo(UserInfoRequest(id))
            if (response.username != getUsername()) {
                dataStore.edit { preference ->
                    preference[username] = response.username
                }
            }
        }
    }

    override suspend fun getUserId(): String {
        return dataStore.data.map {
            it[userId] ?: ""
        }.first()
    }

    override suspend fun setUserId(newUserId: String) {
        dataStore.edit { preference ->
            preference[userId] = newUserId
        }
    }

    override suspend fun getEmail(): String {
        return dataStore.data.map {
            it[email] ?: ""
        }.first()
    }

    override suspend fun setEmail(newEmail: String?) {
        if (newEmail != null) {
            dataStore.edit {
                it[email] = newEmail
            }
        } else {
            val id = getUserId()
            val response = api.getUserInfo(UserInfoRequest(id))
            if (response.email != getEmail()) {
                dataStore.edit {
                    it[email] = response.email
                }
            }
        }
    }

    override suspend fun clear() {
        dataStore.edit {
            it.clear()
        }
    }
}