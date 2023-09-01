package com.ikuzMirel.flick.ui.authentication

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.WorkManager
import com.ikuzMirel.flick.data.constants.LOGIN_CONFLICTED
import com.ikuzMirel.flick.data.constants.USERNAME_CONFLICTED
import com.ikuzMirel.flick.data.repositories.AuthRepository
import com.ikuzMirel.flick.data.repositories.PreferencesRepository
import com.ikuzMirel.flick.data.requests.LoginRequest
import com.ikuzMirel.flick.data.requests.SignupRequest
import com.ikuzMirel.flick.data.response.BasicResponse
import com.ikuzMirel.flick.worker.UnreadMessageWorker
import com.ikuzMirel.flick.worker.WebSocketWorker
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val preferencesRepository: PreferencesRepository,
    private val workManager: WorkManager
) : ViewModel() {

    private val _uiState: MutableStateFlow<AuthUIState> = MutableStateFlow(AuthUIState())
    val uiState: StateFlow<AuthUIState> = _uiState.asStateFlow()

    private val resultChannel = Channel<BasicResponse<String>>()
    val authResult = resultChannel.receiveAsFlow()

    fun onLoginUsernameChange(username: String) {
        _uiState.update {
            it.copy(
                loginUsername = username
            )
        }
    }

    fun onLoginPasswordChange(password: String) {
        _uiState.update {
            it.copy(
                LoginPassword = password
            )
        }
    }

    fun onSignUpEmailChange(email: String) {
        _uiState.update {
            it.copy(
                signUpEmail = email
            )
        }
    }

    fun onSignUpUsernameChange(username: String) {
        _uiState.update {
            it.copy(
                signUpUsername = username
            )
        }
    }

    fun onSignUpPasswordChange(password: String) {
        _uiState.update {
            it.copy(
                signUpPassword = password
            )
        }
    }

    fun onSignUpPasswordConfirmChange(password: String) {
        _uiState.update {
            it.copy(
                signUpConfirmPassword = password
            )
        }
    }

    fun checkToken() {
        viewModelScope.launch {
            preferencesRepository.getValue(PreferencesRepository.TOKEN) ?: run {
                _uiState.update {
                    it.copy(
                        hasToken = false
                    )
                }
                return@launch
            }
            _uiState.update {
                it.copy(
                    hasToken = true
                )
            }
        }
    }

    fun login() {
        viewModelScope.launch {
            _uiState.update {
                it.copy( isLoading = true )
            }
            delay(1000)
            val result = authRepository.login(
                LoginRequest(
                    _uiState.value.loginUsername,
                    _uiState.value.LoginPassword
                )
            )
            result.collect {
                val isLoginError = it is BasicResponse.Error && it.errorMessage == LOGIN_CONFLICTED
                if (isLoginError) {
                    _uiState.update {
                        it.copy(
                            usernameError = true,
                            usernameErrorMessage = LOGIN_CONFLICTED,
                            passwordError = true,
                            passwordErrorMessage = LOGIN_CONFLICTED
                        )
                    }
                }
                resultChannel.send(it)
            }
            _uiState.update {
                it.copy( isLoading = false )
            }
        }
    }

    fun signUp() {
        viewModelScope.launch {
            _uiState.update {
                it.copy( isLoading = true )
            }

            delay(1000)
            isEmailValid()
            isPasswordValid()
            isConfirmPassValid()
            val isError = _uiState.value.emailError || _uiState.value.passwordError || _uiState.value.confirmPasswordError
            if (isError) {
                _uiState.update {
                    it.copy( isLoading = false )
                }
                return@launch
            }

            val request = SignupRequest(
                _uiState.value.signUpUsername,
                _uiState.value.signUpPassword,
                _uiState.value.signUpEmail
            )
            authRepository.signUp(request).collect { response ->
                if (response is BasicResponse.Error) {
                    if (response.errorMessage == USERNAME_CONFLICTED) {
                        _uiState.update {
                            it.copy(
                                usernameError = true,
                                usernameErrorMessage = USERNAME_CONFLICTED
                            )
                        }
                    }
                    resultChannel.send(response)
                    return@collect
                }

                val loginRequest = LoginRequest(
                    _uiState.value.signUpUsername,
                    _uiState.value.signUpPassword
                )
                authRepository.login(loginRequest).collect {
                    resultChannel.send(it)
                }
            }

            _uiState.update {
                it.copy( isLoading = false )
            }
        }
    }

    fun authenticate() {
        viewModelScope.launch {
            authRepository.authenticate().collect {
                resultChannel.send(it)
            }
        }
    }

    fun startWorkManager() {
        workManager.apply {
            enqueueUniquePeriodicWork(
                UnreadMessageWorker.WORK_NAME,
                ExistingPeriodicWorkPolicy.CANCEL_AND_REENQUEUE,
                UnreadMessageWorker.startWork()
            )
            enqueueUniquePeriodicWork(
                WebSocketWorker.WORK_NAME,
                ExistingPeriodicWorkPolicy.CANCEL_AND_REENQUEUE,
                WebSocketWorker.startWork()
            )
        }
    }

    private fun isEmailValid() {
        if (!_uiState.value.signUpEmail.contains("@")) {
            _uiState.update {
                it.copy(
                    emailError = true,
                    emailErrorMessage = "Must contain @"
                )
            }
        }
    }

    private fun isPasswordValid() {
        if (_uiState.value.signUpPassword.length < 8) {
            _uiState.update {
                it.copy(
                    passwordError = true,
                    passwordErrorMessage = "Must contain at least 8 characters"
                )
            }
            return
        }
        if (!_uiState.value.signUpPassword.contains(Regex("[0-9]"))) {
            _uiState.update {
                it.copy(
                    passwordError = true,
                    passwordErrorMessage = "Must contain at least 1 number"
                )
            }
            return
        }
        if (!_uiState.value.signUpPassword.contains(Regex("[A-Z]"))) {
            _uiState.update {
                it.copy(
                    passwordError = true,
                    passwordErrorMessage = "Must contain at least 1 uppercase letter"
                )
            }
        }
    }

    private fun isConfirmPassValid() {
        if (_uiState.value.signUpConfirmPassword != _uiState.value.signUpPassword) {
            _uiState.update {
                it.copy(
                    confirmPasswordError = true,
                    confirmPasswordErrorMessage = "Make sure it matches the password"
                )
            }
        }
    }

    fun clearError() {
        _uiState.update {
            it.copy(
                usernameError = false,
                usernameErrorMessage = "",
                passwordError = false,
                passwordErrorMessage = "",
                confirmPasswordError = false,
                confirmPasswordErrorMessage = ""
            )
        }
    }
}