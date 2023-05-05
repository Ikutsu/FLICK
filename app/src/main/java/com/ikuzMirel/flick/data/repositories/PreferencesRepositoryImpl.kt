package com.ikuzMirel.flick.data.repositories

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.ikuzMirel.flick.data.utils.ResponseResult
import com.ikuzMirel.flick.data.utils.UNABLE_TO_SAVE
import kotlinx.coroutines.flow.*
import javax.inject.Inject

//TODO: implement network status observer in viewmodel not in repository
class PreferencesRepositoryImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : PreferencesRepository {

    companion object {
        private val jwt = stringPreferencesKey("JWT")
        private val username = stringPreferencesKey("username")
        private val userId = stringPreferencesKey("userId")
        private val email = stringPreferencesKey("email")
        private val isFirstTime = booleanPreferencesKey("isFirstTime")
    }

    override suspend fun getJwt() = getValue(jwt)
    override suspend fun setJwt(token: String) = setValue(jwt, token)

    override suspend fun getUsername() = getValue(username)
    override suspend fun setUsername(newUsername: String) = setValue(username, newUsername)

    override suspend fun getUserId() = getValue(userId)
    override suspend fun setUserId(newUserId: String) = setValue(userId, newUserId)

    override suspend fun getEmail() = getValue(email)
    override suspend fun setEmail(newEmail: String) = setValue(email, newEmail)

    override suspend fun getIsFirstTime() = getValue(isFirstTime)
    override suspend fun setIsFirstTime(newValue : Boolean) = setValue(isFirstTime, newValue)

    override suspend fun clear(): ResponseResult<String> {
        dataStore.edit {
            it.clear()
        }
        return ResponseResult.success()
    }

    private suspend fun <T : Any> getValue(key: Preferences.Key<T>): ResponseResult.Success<T> {
        return try {
            val value = dataStore.data.map {
                it[key]
            }.first()
            ResponseResult.success(value)
        } catch (e: Exception) {
            ResponseResult.success(null)
        }
    }

    private suspend fun <T : Any> setValue(
        key: Preferences.Key<T>,
        value: T
    ): ResponseResult<String> {
        return try {
            dataStore.edit {
                it[key] = value
            }
            ResponseResult.success()
        } catch (e: Exception) {
            ResponseResult.error(UNABLE_TO_SAVE)
        }
    }
}