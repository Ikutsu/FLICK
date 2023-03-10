package com.ikuzMirel.flick.ui.authentication

sealed class AuthUIEvent{
    data class SignInUsernameChanged(val username: String) : AuthUIEvent()
    data class SignInPasswordChanged(val password: String) : AuthUIEvent()
    object SignIn : AuthUIEvent()

    data class SignUpEmailChanged(val email: String) : AuthUIEvent()
    data class SignUpUsernameChanged(val username: String) : AuthUIEvent()
    data class SignUpPasswordChanged(val password: String) : AuthUIEvent()
    data class SignUpConfirmPasswordChanged(val confirmPassword: String) : AuthUIEvent()
    object SignUp : AuthUIEvent()
}
