package com.ikuzMirel.flick.ui.authentication

import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Mail
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.focus.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ikuzMirel.flick.R
import com.ikuzMirel.flick.data.constants.LOGIN_CONFLICTED
import com.ikuzMirel.flick.data.constants.USERNAME_CONFLICTED
import com.ikuzMirel.flick.data.response.BasicResponse
import com.ikuzMirel.flick.ui.components.icons.KeyOutline
import com.ikuzMirel.flick.ui.components.textFields.IconHintTextField
import com.ikuzMirel.flick.ui.destinations.*
import com.ikuzMirel.flick.ui.extension.noRippleClickable
import com.ikuzMirel.flick.ui.theme.*
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Destination
@Composable
fun Authentication(
    navigator: DestinationsNavigator,
    viewModel: AuthViewModel = hiltViewModel(),
    _isLogin: Boolean = false
) {
    var isLogin by remember { mutableStateOf(_isLogin) }
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    val emailFocusRequester = remember { FocusRequester() }
    val usernameFocusRequester = remember { FocusRequester() }
    val passwordFocusRequester = remember { FocusRequester() }
    val confirmPassFocusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp

    val fieldHeight by animateDpAsState(
        targetValue = if (isLogin) 0.dp else 78.dp,
        animationSpec = tween(200)
    )
    val fieldAlpha by animateFloatAsState(
        targetValue = if (isLogin) 0f else 1f,
        animationSpec = tween(150)
    )
    val buttonWidth by animateDpAsState(
        targetValue = if (state.isLoading) 54.dp else screenWidth - 104.dp,
        animationSpec = tween(200)
    )
    val buttonRadius by animateDpAsState(
        targetValue = if (state.isLoading) 100.dp else 15.dp,
        animationSpec = tween(200)
    )
    val textAlpha by animateFloatAsState(
        targetValue = if (state.isLoading) 0f else 1f,
        animationSpec = when (state.isLoading) {
            true -> tween(100, delayMillis = 40)
            false -> tween(100, delayMillis = 60)
        }
    )
    val progressAlpha by animateFloatAsState(
        targetValue = if (state.isLoading) 1f else 0f,
        animationSpec = tween(100, delayMillis = 50)
    )


    LaunchedEffect(state, context) {
        viewModel.authResult.collect { result ->
            when (result) {
                is BasicResponse.Success -> {
                    viewModel.startWorkManager()
                    navigator.navigate(HomeDestination) {
                        popUpTo(AuthenticationDestination.route) {
                            inclusive = true
                        }
                        navigator.clearBackStack(AuthenticationDestination)
                    }
                }

                is BasicResponse.Error -> {
                    if (result.errorMessage != LOGIN_CONFLICTED && result.errorMessage != USERNAME_CONFLICTED) {
                        Toast.makeText(
                            context,
                            result.errorMessage,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }

    Surface() {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectTapGestures(onTap = {
                        focusManager.clearFocus()
                    })
                }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .verticalScroll(scrollState),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.weight(1f))
                IconTitle(
                    title = when (isLogin) {
                        true -> "Log in"
                        false -> "Sign up"
                    }
                )
                Spacer(modifier = Modifier.height(24.dp))
                IconHintTextField(
                    leadingIcon = Icons.Outlined.Mail,
                    gotFocusIcon = Icons.Filled.Mail,
                    placeholder = "E-mail",
                    imeAction = ImeAction.Next,
                    value = state.signUpEmail,
                    onValueChange = { viewModel.onSignUpEmailChange(it) },
                    focusRequester = emailFocusRequester,
                    keyboardActions = KeyboardActions(onNext = {
                        usernameFocusRequester.requestFocus()
                    }),
                    error = state.emailError,
                    errorMsg = state.emailErrorMessage,
                    height = fieldHeight,
                    alpha = fieldAlpha
                )
                IconHintTextField(
                    leadingIcon = Icons.Outlined.Person,
                    gotFocusIcon = Icons.Filled.Person,
                    placeholder = "Username",
                    imeAction = ImeAction.Next,
                    value = when (isLogin) {
                        true -> state.loginUsername
                        false -> state.signUpUsername
                    },
                    onValueChange = {
                        when (isLogin) {
                            true -> viewModel.onLoginUsernameChange(it)
                            false -> viewModel.onSignUpUsernameChange(it)
                        }
                    },
                    focusRequester = usernameFocusRequester,
                    error = state.usernameError,
                    errorMsg = state.usernameErrorMessage,
                    keyboardActions = KeyboardActions(onNext = {
                        passwordFocusRequester.requestFocus()
                    }),
                )
                IconHintTextField(
                    leadingIcon = Icons.Outlined.KeyOutline,
                    gotFocusIcon = Icons.Filled.Key,
                    placeholder = "Password",
                    imeAction = when (isLogin) {
                        true -> ImeAction.Done
                        false -> ImeAction.Next
                    },
                    value = when (isLogin) {
                        true -> state.LoginPassword
                        false -> state.signUpPassword
                    },
                    onValueChange = {
                        when (isLogin) {
                            true -> viewModel.onLoginPasswordChange(it)
                            false -> viewModel.onSignUpPasswordChange(it)
                        }
                    },
                    focusRequester = passwordFocusRequester,
                    keyboardActions = when (isLogin) {
                        true -> KeyboardActions(onDone = {
                            focusManager.clearFocus()
                        })

                        false -> KeyboardActions(onNext = {
                            confirmPassFocusRequester.requestFocus()
                        })
                    },
                    error = state.passwordError,
                    errorMsg = state.passwordErrorMessage,
                    isPassword = true,
                )
                IconHintTextField(
                    leadingIcon = Icons.Outlined.KeyOutline,
                    gotFocusIcon = Icons.Filled.Key,
                    placeholder = "Confirm Password",
                    imeAction = ImeAction.Done,
                    value = state.signUpConfirmPassword,
                    onValueChange = {
                        viewModel.onSignUpPasswordConfirmChange(it)
                    },
                    focusRequester = confirmPassFocusRequester,
                    keyboardActions = KeyboardActions(onDone = {
                        focusManager.clearFocus()
                    }),
                    error = state.confirmPasswordError,
                    errorMsg = state.confirmPasswordErrorMessage,
                    isPassword = true,
                    height = fieldHeight,
                    alpha = fieldAlpha
                )
                BasicButton(
                    onClick = {
                        focusManager.clearFocus()
                        viewModel.clearError()
                        when (isLogin) {
                            true -> viewModel.login()
                            false -> viewModel.signUp()
                        }
                    },
                    text = when (isLogin) {
                        true -> "Log in"
                        false -> "Sign up"
                    },
                    enabled = when (isLogin) {
                        true -> state.loginUsername.isNotBlank()
                                && state.LoginPassword.isNotBlank()

                        false -> state.signUpUsername.isNotBlank()
                                && state.signUpEmail.isNotBlank()
                                && state.signUpPassword.isNotBlank()
                                && state.signUpConfirmPassword.isNotBlank()
                    },
                    buttonWidth = buttonWidth,
                    buttonRadius = buttonRadius,
                    textAlpha = textAlpha,
                    progressAlpha = progressAlpha,
                )
                Spacer(modifier = Modifier.weight(1f))
                BottomText(isLogin, action = {
                    viewModel.clearError()
                    isLogin = !isLogin
                })
            }
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun IconTitle(
    title: String
) {
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(
                id = R.drawable.flick_welcome_light
//                when {
//                    isSystemInDarkTheme() -> R.drawable.flick_4x
//                    else -> R.drawable.flick_dark
//                }
            ),
            contentDescription = "",
            modifier = Modifier
                .width(48.dp)
        )
        Spacer(modifier = Modifier.height(14.dp))
        AnimatedContent(
            targetState = title,
            transitionSpec = {
                fadeIn(animationSpec = tween(200, delayMillis = 50)) +
                        scaleIn(
                            initialScale = 0.32f,
                            animationSpec = tween(200, delayMillis = 50)
                        ) with fadeOut(animationSpec = tween(50))
            }
        ) {
            Text(
                text = it,
                fontSize = 24.sp,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun BottomText(
    isLogin: Boolean,
    action: () -> Unit
) {
    val firstText = when (isLogin) {
        true -> "Don't have an account? "
        false -> "Already have an account? "
    }
    val buttonText = when (isLogin) {
        true -> "Sign up"
        false -> "Log in"
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(72.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        AnimatedContent(
            targetState = firstText,
            transitionSpec = {
                fadeIn() with fadeOut()
            }
        ) {
            Text(
                text = it,
                fontSize = 12.sp,
                color = Gray50,
            )
        }
        AnimatedContent(
            targetState = buttonText,
            transitionSpec = {
                fadeIn() with fadeOut()
            }
        ) {
            Text(
                text = it,
                color = Purple60,
                modifier = Modifier
                    .noRippleClickable { action() },
                fontSize = 12.sp,
                textDecoration = TextDecoration.Underline
            )

        }
    }
}

@Composable
fun BasicButton(
    onClick: () -> Unit,
    text: String,
    enabled: Boolean = true,
    buttonWidth: Dp,
    buttonRadius: Dp,
    textAlpha: Float,
    progressAlpha: Float
) {
    Box(
        modifier = Modifier.fillMaxHeight(),
        contentAlignment = Alignment.Center
    ) {
        Button(
            onClick = onClick,
            modifier = Modifier
                .width(buttonWidth)
                .height(54.dp),
            shape = RoundedCornerShape(buttonRadius),
            colors = ButtonDefaults.buttonColors(
                containerColor = Red60,
                disabledContainerColor = Red60.copy(alpha = 0.3f)
            ),
            enabled = enabled
        ) {
            Text(
                modifier = Modifier.alpha(textAlpha),
                text = text,
                color = when (enabled) {
                    true -> Color.White
                    false -> Color.White.copy(alpha = 0.5f)
                },
                fontSize = 18.sp,
            )
        }
        CircularProgressIndicator(
            modifier = Modifier
                .size(36.dp)
                .alpha(progressAlpha),
            strokeWidth = 4.dp,
            color = Color.White,
            strokeCap = StrokeCap.Round
        )
    }
}