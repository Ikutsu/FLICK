package com.ikuzMirel.flick.ui.authentication

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ikuzMirel.flick.data.auth.AuthRepository
import com.ikuzMirel.flick.data.auth.AuthResult
import com.ikuzMirel.flick.data.user.UserPreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel(){

    var state by mutableStateOf(AuthState())

    private val resultChannel = Channel<AuthResult<Unit>>()
    val authResult = resultChannel.receiveAsFlow()

    init {
        state = state.copy(isInit = true)
        println("AuthViewModel init")
        viewModelScope.launch {
            state = state.copy(hasToken = userPreferencesRepository.getJwt().isNotBlank())
        }
        authenticate()
        state = state.copy(isInit = false)
    }

    fun onEvent (event: AuthUIEvent) {
        when (event) {
            is AuthUIEvent.SignInUsernameChanged -> state = state.copy(signInUsername = event.username)
            is AuthUIEvent.SignInPasswordChanged -> state = state.copy(signInPassword = event.password)
            is AuthUIEvent.SignIn -> { signIn() }
            is AuthUIEvent.SignUpEmailChanged -> state = state.copy(signUpEmail = event.email)
            is AuthUIEvent.SignUpUsernameChanged -> state = state.copy(signUpUsername = event.username)
            is AuthUIEvent.SignUpPasswordChanged -> state = state.copy(signUpPassword = event.password)
            is AuthUIEvent.SignUpConfirmPasswordChanged -> state = state.copy(signUpConfirmPassword = event.confirmPassword)
            is AuthUIEvent.SignUp -> { signUp() }
        }
    }

    private fun signIn() {
        viewModelScope.launch {
            state = state.copy(isLoading = true)
            delay(1000)
            val result = authRepository.signIn(
                state.signInUsername,
                state.signInPassword
            )
            if (result is AuthResult.LoginConflicted) {
                state = state.copy(
                    usernameError = true,
                    usernameErrorMessage = "Incorrect username or password",
                    passwordError = true,
                    passwordErrorMessage = "Incorrect username or password"
                )
            }
            resultChannel.send(result)
            state = state.copy(isLoading = false)
        }
    }

    private fun signUp() {
        viewModelScope.launch {
            state = state.copy(isLoading = true)
            delay(1000)
            isEmailValid()
            isPasswordValid()
            isConfirmPassValid()
            if (state.emailError || state.passwordError || state.confirmPasswordError) {
                state = state.copy(isLoading = false)
                return@launch
            }
            val result = authRepository.signUp(
                state.signUpUsername,
                state.signUpPassword,
                state.signUpEmail
            )
            isUsernameValid(result)
            resultChannel.send(result)
            state = state.copy(isLoading = false)
        }
    }

    private fun authenticate() {
        viewModelScope.launch {
            val result = authRepository.authenticate()
            resultChannel.send(result)
        }
    }

    private fun isEmailValid() {
        if (!state.signUpEmail.contains("@")) {
            state = state.copy(
                emailError = true,
                emailErrorMessage = "Must contain @"
            )
            return
        }
        state = state.copy(
            emailError = false,
            emailErrorMessage = ""
        )
    }

    private fun isUsernameValid(result: AuthResult<Unit>) {
        if (result is AuthResult.UsernameConflicted) {
            state = state.copy(
                usernameError = true,
                usernameErrorMessage = "Username already exists"
            )
            return
        }
        state = state.copy(
            usernameError = false,
            usernameErrorMessage = ""
        )
    }

    private fun isPasswordValid() {
        if (state.signUpPassword.length < 8) {
            state = state.copy(
                passwordError = true,
                passwordErrorMessage = "Must contain at least 8 characters"
            )
            return
        }
        if (!state.signUpPassword.contains(Regex("[0-9]"))) {
            state = state.copy(
                passwordError = true,
                passwordErrorMessage = "Must contain at least 1 number"
            )
            return
        }
        if (!state.signUpPassword.contains(Regex("[A-Z]"))) {
            state = state.copy(
                passwordError = true,
                passwordErrorMessage = "Must contain at least 1 uppercase letter"
            )
            return
        }
        state = state.copy(
            passwordError = false,
            passwordErrorMessage = ""
        )
    }

    private fun isConfirmPassValid() {
        if (state.signUpConfirmPassword != state.signUpPassword){
            state = state.copy(
                confirmPasswordError = true,
                confirmPasswordErrorMessage = "Make sure it matches the password"
            )
            return
        }
        state = state.copy(
            confirmPasswordError = false,
            confirmPasswordErrorMessage = ""
        )
    }

    fun clearError() {
        state = state.copy(
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