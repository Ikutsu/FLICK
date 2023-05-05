package com.ikuzMirel.flick.ui.authentication

sealed class AuthUIEvent{
    data class LoginUsernameChanged(val username: String) : AuthUIEvent()
    data class LoginPasswordChanged(val password: String) : AuthUIEvent()
    object Login : AuthUIEvent()

    data class SignUpEmailChanged(val email: String) : AuthUIEvent()
    data class SignUpUsernameChanged(val username: String) : AuthUIEvent()
    data class SignUpPasswordChanged(val password: String) : AuthUIEvent()
    data class SignUpConfirmPasswordChanged(val confirmPassword: String) : AuthUIEvent()
    object SignUp : AuthUIEvent()
}
