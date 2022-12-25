package com.ikuzMirel.flick.ui.login

import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ikuzMirel.flick.ui.components.icons.KeyOutline
import com.ikuzMirel.flick.ui.components.textFields.IconHintTextField
import com.ikuzMirel.flick.ui.components.topAppBars.NavOnlyTopBar
import com.ikuzMirel.flick.ui.sighUp.BasicButton
import com.ikuzMirel.flick.ui.sighUp.IconTitle
import com.ikuzMirel.flick.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Login() {
    val focusManager = LocalFocusManager.current
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures(onTap = {
                    focusManager.clearFocus()
                })
            },
        topBar = { NavOnlyTopBar(
            navOnClick = {}
        ) },
        bottomBar = { BottomText() }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Spacer(modifier = Modifier.height(24.dp))
            IconTitle(
                title = "Log in"
            )
            Spacer(modifier = Modifier.height(24.dp))
            Content()
            Spacer(modifier = Modifier.height(24.dp))
            BasicButton(onClick = {}, text = "Log in")
        }
    }
}

@Composable
private fun Content() {
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
        })
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
        })
    )
}

@Composable
private fun BottomText() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(72.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ){
        Text(
            text = "Don’t have an account? ",
            fontSize = 16.sp,
            color = Gray50,
            fontFamily = cocogooseLight,
        )
        Text(
            text = "Sign up",
            color = Purple60,
            modifier = Modifier
                .clickable {  },
            fontSize = 16.sp,
            fontFamily = cocogooseSemiLight,
            textDecoration = TextDecoration.Underline
        )
    }
}