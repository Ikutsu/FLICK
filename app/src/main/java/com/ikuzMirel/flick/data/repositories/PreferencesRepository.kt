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
        val TOKEN = stringPreferencesKey("JWT")
        val USERNAME = stringPreferencesKey("username")
        val USERID = stringPreferencesKey("userId")
        val EMAIL = stringPreferencesKey("email")
        val FIRST_TIME = booleanPreferencesKey("isFirstTime")
    }

    suspend fun clearUserData() {
        dataStore.edit {
            it[TOKEN] = ""
            it[USERNAME] = ""
            it[USERID] = ""
            it[EMAIL] = ""
        }
    }

    suspend fun <T> getValue(key: Preferences.Key<T>): T? {
        return dataStore.data.map { it[key] }.first()
    }

    suspend fun <T> setValue(
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