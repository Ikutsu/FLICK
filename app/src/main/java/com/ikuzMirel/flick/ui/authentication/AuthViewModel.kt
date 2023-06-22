package com.ikuzMirel.flick.ui.authentication

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ikuzMirel.flick.data.constants.LOGIN_CONFLICTED
import com.ikuzMirel.flick.data.constants.USERNAME_CONFLICTED
import com.ikuzMirel.flick.data.repositories.AuthRepository
import com.ikuzMirel.flick.data.repositories.PreferencesRepository
import com.ikuzMirel.flick.data.requests.LoginRequest
import com.ikuzMirel.flick.data.requests.SignupRequest
import com.ikuzMirel.flick.data.response.BasicResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val preferencesRepository: PreferencesRepository
) : ViewModel() {

    val state = mutableStateOf(AuthUIState())

    private val resultChannel = Channel<BasicResponse<String>>()
    val authResult = resultChannel.receiveAsFlow()

    fun checkToken() {
        viewModelScope.launch {
            when (preferencesRepository.getJwt()) {
                "" -> {
                    state.value = state.value.copy(
                        hasToken = false
                    )
                }
                else -> {
                    state.value = state.value.copy(
                        hasToken = true
                    )
                }
            }
        }
    }

    fun onEvent(event: AuthUIEvent) {
        when (event) {
            is AuthUIEvent.LoginUsernameChanged -> state.value =
                state.value.copy(loginUsername = event.username)
            is AuthUIEvent.LoginPasswordChanged -> state.value =
                state.value.copy(LoginPassword = event.password)
            is AuthUIEvent.Login -> {
                login()
            }
            is AuthUIEvent.SignUpEmailChanged -> state.value =
                state.value.copy(signUpEmail = event.email)
            is AuthUIEvent.SignUpUsernameChanged -> state.value =
                state.value.copy(signUpUsername = event.username)
            is AuthUIEvent.SignUpPasswordChanged -> state.value =
                state.value.copy(signUpPassword = event.password)
            is AuthUIEvent.SignUpConfirmPasswordChanged -> state.value =
                state.value.copy(signUpConfirmPassword = event.confirmPassword)
            is AuthUIEvent.SignUp -> {
                signUp()
            }
        }
    }

    private fun login() {
        viewModelScope.launch {
            state.value = state.value.copy(isLoading = true)
            delay(1000)
            val result = authRepository.login(
                LoginRequest(
                    state.value.loginUsername,
                    state.value.LoginPassword
                )
            )
            result.collect{
                if (it is BasicResponse.Error && it.errorMessage == LOGIN_CONFLICTED) {
                    state.value = state.value.copy(
                        usernameError = true,
                        usernameErrorMessage = LOGIN_CONFLICTED,
                        passwordError = true,
                        passwordErrorMessage = LOGIN_CONFLICTED
                    )
                }
                resultChannel.send(it)
            }
            state.value = state.value.copy(isLoading = false)
        }
    }

    private fun signUp() {
        viewModelScope.launch {
            state.value = state.value.copy(isLoading = true)
            delay(1000)
            isEmailValid()
            isPasswordValid()
            isConfirmPassValid()
            if (state.value.emailError || state.value.passwordError || state.value.confirmPasswordError) {
                state.value = state.value.copy(isLoading = false)
                return@launch
            }
            val request = SignupRequest(
                state.value.signUpUsername,
                state.value.signUpPassword,
                state.value.signUpEmail
            )
            authRepository.signUp(request).collect{ response ->
                if (response is BasicResponse.Error) {
                    if (response.errorMessage == USERNAME_CONFLICTED){
                        state.value = state.value.copy(
                            usernameError = true,
                            usernameErrorMessage = USERNAME_CONFLICTED
                        )
                    }
                    resultChannel.send(response)
                    return@collect
                }

                val loginRequest = LoginRequest(
                    state.value.signUpUsername,
                    state.value.signUpPassword
                )
                authRepository.login(loginRequest).collect{
                    resultChannel.send(it)
                }
            }
            state.value = state.value.copy(isLoading = false)
        }
    }

    fun authenticate() {
        viewModelScope.launch {
            authRepository.authenticate().collect{
                resultChannel.send(it)
            }
        }
    }

    private fun isEmailValid() {
        if (!state.value.signUpEmail.contains("@")) {
            state.value = state.value.copy(
                emailError = true,
                emailErrorMessage = "Must contain @"
            )
        }
    }

    private fun isPasswordValid() {
        if (state.value.signUpPassword.length < 8) {
            state.value = state.value.copy(
                passwordError = true,
                passwordErrorMessage = "Must contain at least 8 characters"
            )
            return
        }
        if (!state.value.signUpPassword.contains(Regex("[0-9]"))) {
            state.value = state.value.copy(
                passwordError = true,
                passwordErrorMessage = "Must contain at least 1 number"
            )
            return
        }
        if (!state.value.signUpPassword.contains(Regex("[A-Z]"))) {
            state.value = state.value.copy(
                passwordError = true,
                passwordErrorMessage = "Must contain at least 1 uppercase letter"
            )
        }
    }

    private fun isConfirmPassValid() {
        if (state.value.signUpConfirmPassword != state.value.signUpPassword) {
            state.value = state.value.copy(
                confirmPasswordError = true,
                confirmPasswordErrorMessage = "Make sure it matches the password"
            )
        }
    }

    fun clearError() {
        state.value = state.value.copy(
            emailError = false,
            emailErrorMessage = "",
            usernameError = false,
            usernameErrorMessage = "",
            passwordError = false,
            passwordErrorMessage = "",
            confirmPasswordError = false,
            confirmPasswordErrorMessage = ""
        )
    }
}