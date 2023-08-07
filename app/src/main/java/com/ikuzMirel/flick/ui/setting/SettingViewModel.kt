package com.ikuzMirel.flick.ui.setting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.WorkManager
import com.ikuzMirel.flick.data.remote.websocket.WebSocketApi
import com.ikuzMirel.flick.data.repositories.PreferencesRepository
import com.ikuzMirel.flick.worker.WebSocketWorker
import dagger.hilt.android.lifecycle.HiltViewModel
import io.ktor.client.HttpClient
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerAuthProvider
import io.ktor.client.plugins.plugin
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(
    private val client: HttpClient,
    private val webSocketApi: WebSocketApi,
    private val preferencesRepository: PreferencesRepository,
    private val workManager: WorkManager
): ViewModel(){

    private val _isReady: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isReady: StateFlow<Boolean> = _isReady.asStateFlow()

    fun logout() {
        viewModelScope.launch {
            webSocketApi.disconnectFromSocket()
            workManager.cancelUniqueWork(WebSocketWorker.WORK_NAME)
            client.plugin(Auth).providers
                .filterIsInstance<BearerAuthProvider>()
                .first().clearToken()
            preferencesRepository.clearUserData()
            _isReady.update { true }
        }
    }

}