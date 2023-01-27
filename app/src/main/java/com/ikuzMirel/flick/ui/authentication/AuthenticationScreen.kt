package com.ikuzMirel.flick.ui.authentication

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
import androidx.compose.ui.focus.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ikuzMirel.flick.R
import com.ikuzMirel.flick.ui.components.icons.KeyOutline
import com.ikuzMirel.flick.ui.components.textFields.IconHintTextField
import com.ikuzMirel.flick.ui.components.topAppBars.NavOnlyTopBar
import com.ikuzMirel.flick.ui.destinations.*
import com.ikuzMirel.flick.ui.theme.*
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Destination
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Authentication(
    navigator: DestinationsNavigator
) {
    var account by remember {
        mutableStateOf(false)
    }
    val focusManager = LocalFocusManager.current
    val scrollState = rememberScrollState()
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures(onTap = {
                    focusManager.clearFocus()
                })
            },
        topBar = { NavOnlyTopBar(
            navOnClick = {navigator.popBackStack()}
        ) },
        bottomBar = { BottomText(account, action = { account = !account }) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(scrollState)
        ) {
            Spacer(modifier = Modifier.height(24.dp))
            IconTitle(
                title = when (account){
                    true -> "Log in"
                    false -> "Sign up"
                }
            )
            Spacer(modifier = Modifier.height(24.dp))
            when (account){
               true -> LoginContent()
                false -> SignUpContent()
            }
            Spacer(modifier = Modifier.height(24.dp))
            BasicButton(onClick = {
                navigator.navigate(MainContentDestination)
            }, text = when (account){
                true -> "Log in"
                false -> "Sign up"
            })
        }
    }
}

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
                id = when {
                    isSystemInDarkTheme() -> R.drawable.flick_4x
                    else -> R.drawable.flick_dark
                }
            ),
            contentDescription = "",
            modifier = Modifier
                .width(48.dp)
        )
        Spacer(modifier = Modifier.height(14.dp))
        Text(
            text = title,
            fontSize = 24.sp,
            color = MaterialTheme.colorScheme.onBackground,
            fontFamily = cocogooseBold
        )
    }
}

@Composable
private fun SignUpContent() {

    val emailValue = remember { mutableStateOf(TextFieldValue("")) }
    val userValue = remember { mutableStateOf(TextFieldValue("")) }
    val passwordValue = remember { mutableStateOf(TextFieldValue("")) }
    val confirmPassValue = remember { mutableStateOf(TextFieldValue("")) }

    val emailFocusRequester = remember { FocusRequester() }
    val usernameFocusRequester = remember { FocusRequester() }
    val passwordFocusRequester = remember { FocusRequester() }
    val confirmPassFocusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    IconHintTextField(
        leadingIcon = Icons.Outlined.Mail,
        gotFocusIcon = Icons.Filled.Mail,
        placeholder = "E-mail",
        imeAction = ImeAction.Next,
        value = emailValue.value,
        onValueChange = { emailValue.value = it },
        focusRequester = emailFocusRequester,
        keyboardActions = KeyboardActions(onNext = {
            usernameFocusRequester.requestFocus()
        }),
        visualTransformation = VisualTransformation.None
    )
    Spacer(modifier = Modifier.height(24.dp))
    IconHintTextField(
        leadingIcon = Icons.Outlined.Person,
        gotFocusIcon = Icons.Filled.Person,
        placeholder = "Username",
        imeAction = ImeAction.Next,
        value = userValue.value,
        onValueChange = { userValue.value = it },
        focusRequester = usernameFocusRequester,
        keyboardActions = KeyboardActions(onNext = {
            passwordFocusRequester.requestFocus()
        }),
        visualTransformation = VisualTransformation.None
    )
    Spacer(modifier = Modifier.height(24.dp))
    IconHintTextField(
        leadingIcon = Icons.Outlined.KeyOutline,
        gotFocusIcon = Icons.Filled.Key,
        placeholder = "Password",
        imeAction = ImeAction.Next,
        value = passwordValue.value,
        onValueChange = { passwordValue.value = it },
        focusRequester = passwordFocusRequester,
        keyboardActions = KeyboardActions(onNext = {
            confirmPassFocusRequester.requestFocus()
        }),
        visualTransformation = PasswordVisualTransformation(),
        textStyle = TextStyle(
            color = MaterialTheme.colorScheme.onBackground,
            letterSpacing = 2.sp
        )
    )
    Spacer(modifier = Modifier.height(24.dp))
    IconHintTextField(
        leadingIcon = Icons.Outlined.KeyOutline,
        gotFocusIcon = Icons.Filled.Key,
        placeholder = "Confirm Password",
        imeAction = ImeAction.Done,
        value = confirmPassValue.value,
        onValueChange = { confirmPassValue.value = it },
        focusRequester = confirmPassFocusRequester,
        keyboardActions = KeyboardActions(onDone = {
            focusManager.clearFocus()
        }),
        visualTransformation = PasswordVisualTransformation(),
        textStyle = TextStyle(
            color = MaterialTheme.colorScheme.onBackground,
            letterSpacing = 2.sp
        )
    )
}

@Composable
private fun BottomText(
    account: Boolean,
    action: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(72.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ){
        Text(
            text =  when (account){
                true -> "Don't have an account? "
                false -> "Already have an account? "
            },
            fontSize = 12.sp,
            color = Gray50,
            fontFamily = cocogooseLight,
        )
        Text(
            text =  when (account){
                true -> "Sign up"
                false -> "Log in"
            },
            color = Purple60,
            modifier = Modifier
                .clickable { action() },
            fontSize = 12.sp,
            fontFamily = cocogooseSemiLight,
            textDecoration = TextDecoration.Underline
        )
    }
}

@Composable
fun BasicButton(
    onClick: () -> Unit,
    text: String
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(54.dp)
            .padding(horizontal = 52.dp),
        shape = RoundedCornerShape(15.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Red60
        )
    ) {
        Text(
            text = text,
            color = Color.White,
            fontSize = 18.sp,
            fontFamily = cocogooseBold
        )
    }
}

@Composable
private fun LoginContent() {
    val userValue = remember { mutableStateOf(TextFieldValue("")) }
    val passwordValue = remember { mutableStateOf(TextFieldValue("")) }

    val usernameFocusRequester = remember { FocusRequester() }
    val passwordFocusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    IconHintTextField(
        leadingIcon = Icons.Outlined.Person,
        gotFocusIcon = Icons.Filled.Person,
        placeholder = "Username",
        imeAction = ImeAction.Next,
        value = userValue.value,
        onValueChange = { userValue.value = it },
        focusRequester = usernameFocusRequester,
        keyboardActions = KeyboardActions(onNext = {
            passwordFocusRequester.requestFocus()
        }),
        visualTransformation = VisualTransformation.None
    )
    Spacer(modifier = Modifier.height(24.dp))
    IconHintTextField(
        leadingIcon = Icons.Outlined.KeyOutline,
        gotFocusIcon = Icons.Filled.Key,
        placeholder = "Password",
        imeAction = ImeAction.Done,
        value = passwordValue.value,
        onValueChange = { passwordValue.value = it },
        focusRequester = passwordFocusRequester,
        keyboardActions = KeyboardActions(onDone = {
            focusManager.clearFocus()
        }),
        visualTransformation = PasswordVisualTransformation(),
        textStyle = TextStyle(
            color = MaterialTheme.colorScheme.onBackground,
            letterSpacing = 2.sp
        )
    )
}