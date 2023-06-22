package com.ikuzMirel.flick.data.repositories

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.ikuzMirel.flick.data.constants.UNABLE_TO_SAVE
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

//TODO: implement network status observer
class PreferencesRepository @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {

    companion object {
        private val jwt = stringPreferencesKey("JWT")
        private val username = stringPreferencesKey("username")
        private val userId = stringPreferencesKey("userId")
        private val email = stringPreferencesKey("email")
        private val isFirstTime = booleanPreferencesKey("isFirstTime")
    }

    suspend fun getJwt(): String = getValue(jwt) ?: ""
    suspend fun setJwt(token: String) = setValue(jwt, token)

    suspend fun getUsername() = getValue(username) ?: ""
    suspend fun setUsername(newUsername: String) = setValue(username, newUsername)

    suspend fun getUserId() = getValue(userId) ?: ""
    suspend fun setUserId(newUserId: String) = setValue(userId, newUserId)

    suspend fun getEmail() = getValue(email) ?: ""
    suspend fun setEmail(newEmail: String) = setValue(email, newEmail)

    suspend fun getIsFirstTime() = getValue(isFirstTime) ?: false
    suspend fun setIsFirstTime(newValue: Boolean) = setValue(isFirstTime, newValue)

    suspend fun clearUserData() {
        dataStore.edit {
            it[jwt] = ""
            it[username] = ""
            it[userId] = ""
            it[email] = ""
        }
    }

    private suspend fun <T : Any> getValue(key: Preferences.Key<T>): T? {
        return dataStore.data.map { it[key] }.first()
    }

    private suspend fun <T : Any> setValue(
        key: Preferences.Key<T>,
        value: T
    ) {
        try {
            dataStore.edit {
                it[key] = value
            }
        } catch (e: Exception) {
            throw Exception(UNABLE_TO_SAVE)
        }
    }
}