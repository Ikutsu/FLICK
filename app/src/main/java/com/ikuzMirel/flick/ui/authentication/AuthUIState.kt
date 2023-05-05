package com.ikuzMirel.flick.ui.authentication

data class AuthUIState (
    val isLoading: Boolean = false,
    var keepOnScreenCondition: Boolean = true,
    val isInit : Boolean = false,
    val hasToken: Boolean = false,

    val loginUsername: String = "test1231",
    val LoginPassword: String = "Qwert1234",

    val signUpEmail: String = "qwe@gmail.com",
    val signUpUsername: String = "test1231",
    val signUpPassword: String = "Qwert1234",
    val signUpConfirmPassword: String = "Qwert1234",

    val emailError: Boolean = false,
    val usernameError: Boolean = false,
    val passwordError: Boolean = false,
    val confirmPasswordError: Boolean = false,

    val emailErrorMessage: String = "Must contain @",
    val usernameErrorMessage: String = "",
    val passwordErrorMessage: String = "",
    val confirmPasswordErrorMessage: String = "Make sure it matches the password",
)