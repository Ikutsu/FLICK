package com.ikuzMirel.flick.ui.setting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ikuzMirel.flick.data.remote.websocket.WebSocketApi
import com.ikuzMirel.flick.data.repositories.PreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(
    private val webSocketApi: WebSocketApi,
    private val preferencesRepository: PreferencesRepository
): ViewModel(){

    private val _isReady: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isReady: StateFlow<Boolean> = _isReady.asStateFlow()

    fun logout() {
        viewModelScope.launch {
            webSocketApi.disconnectFromSocket()
            preferencesRepository.clearUserData()
            _isReady.value = true
        }
    }
}