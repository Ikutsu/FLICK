package com.ikuzMirel.flick.ui.authentication

data class AuthUIState (
    val isLoading: Boolean = false,
    var keepOnScreenCondition: Boolean = true,
    val isInit : Boolean = false,
    val hasToken: Boolean = false,

    val loginUsername: String = "",
    val LoginPassword: String = "",

    val signUpEmail: String = "",
    val signUpUsername: String = "",
    val signUpPassword: String = "",
    val signUpConfirmPassword: String = "",

    val emailError: Boolean = false,
    val usernameError: Boolean = false,
    val passwordError: Boolean = false,
    val confirmPasswordError: Boolean = false,

    val emailErrorMessage: String = "Must contain @",
    val usernameErrorMessage: String = "",
    val passwordErrorMessage: String = "",
    val confirmPasswordErrorMessage: String = "Make sure it matches the password",
)