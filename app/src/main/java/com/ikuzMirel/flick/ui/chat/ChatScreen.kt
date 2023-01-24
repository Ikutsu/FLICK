package com.ikuzMirel.flick.ui.chat

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.AttachFile
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ikuzMirel.flick.R
import com.ikuzMirel.flick.data.demo.massageList
import com.ikuzMirel.flick.data.model.MessageModel
import com.ikuzMirel.flick.ui.components.icons.SendOutlined
import com.ikuzMirel.flick.ui.extension.noRippleClickable
import com.ikuzMirel.flick.ui.theme.*

// TODO: Hardcoded data source, change when viewModel is created
// TODO: Unconfirmed font, current: Museo sans 
@Composable
fun Chat() {
    val focusManager = LocalFocusManager.current
    val scrollState = rememberLazyListState()
    Surface(
        modifier = Modifier
            .windowInsetsPadding(
                WindowInsets
                    .navigationBars
                    .only(WindowInsetsSides.Horizontal + WindowInsetsSides.Top)
            )
            .pointerInput(Unit) {
                detectTapGestures(onTap = {
                    focusManager.clearFocus()
                })
            },
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()

        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                Content(
                    messages = massageList,
                    modifier = Modifier
                        .weight(1f),
                    scrollState = scrollState
                )
                UserInputField()
            }
            TitleAvatarTopBar(
            )
        }

    }
}

@Composable
private fun Content(
    messages: List<MessageModel>,
    modifier: Modifier,
    scrollState: LazyListState
) {
    Box(modifier = modifier) {
        LazyColumn(
            modifier = Modifier.wrapContentSize(),
            state = scrollState,
            reverseLayout = true,
            contentPadding = WindowInsets.statusBars.add(WindowInsets(top = 76.dp))
                .asPaddingValues()
        ) {
            items(messages) {
                Message(
                    userMe = "Xuan",
                    massage = it
                )
            }
        }

    }
}

@Composable
fun Message(
    userMe: String,
    massage: MessageModel
) {
    val isOwnMessage = massage.user == userMe
    val (showTime, setShowTime) = remember {
        mutableStateOf(false)
    }
    if (isOwnMessage) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 60.dp, end = 16.dp, top = 4.dp, bottom = 4.dp),
            horizontalArrangement = Arrangement.End
        ) {
            Column(
                modifier = Modifier
                    .background(Purple50, RoundedCornerShape(12.dp))
                    .noRippleClickable { setShowTime(!showTime) }
                    .animateContentSize()
            ) {
                Text(
                    text = massage.content,
                    color = Color.White,
                    modifier = Modifier
                        .padding(
                            start = 14.dp,
                            end = 14.dp,
                            top = 14.dp,
                            bottom = if (showTime) 0.dp else 14.dp
                        ),
                    fontFamily = museoRegular
                )
                if (showTime) {
                    Text(
                        text = massage.time,
                        color = MaterialTheme.colorScheme.onBackground,
                        fontSize = 12.sp,
                        modifier = Modifier
                            .padding(start = 6.dp, bottom = 4.dp),
                        fontFamily = museoRegular
                    )
                }
            }
            Spacer(modifier = Modifier.width(8.dp))
            Image(
                painter = painterResource(id = R.drawable.avatar),
                contentDescription = "",
                modifier = Modifier
                    .size(36.dp)
            )
        }
    } else {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 60.dp, top = 4.dp, bottom = 4.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.avatar),
                contentDescription = "",
                modifier = Modifier
                    .size(36.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column(
                modifier = Modifier
                    .background(Red50, RoundedCornerShape(12.dp))
                    .noRippleClickable { setShowTime(!showTime) }
                    .animateContentSize()
            ) {
                Text(
                    text = massage.content,
                    color = Color.White,
                    modifier = Modifier
                        .padding(
                            start = 14.dp,
                            end = 14.dp,
                            top = 14.dp,
                            bottom = if (showTime) 0.dp else 14.dp
                        ),
                    fontFamily = museoRegular
                )
                if (showTime) {
                    Text(
                        text = massage.time,
                        color = MaterialTheme.colorScheme.onBackground,
                        fontSize = 12.sp,
                        modifier = Modifier
                            .padding(end = 6.dp, bottom = 4.dp)
                            .align(Alignment.End),
                        fontFamily = museoRegular
                    )
                }
            }
        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TitleAvatarTopBar(
) {
    CenterAlignedTopAppBar(
        navigationIcon = {
            IconButton(
                onClick = { /*TODO*/ }
            ) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = ""
                )
            }
        },
        title = { Text(
            text = "Mirel",
            fontFamily = museoRegular
        ) },
        actions = {
            IconButton(
                onClick = { /*TODO*/ }
            ) {
                Icon(
                    imageVector = Icons.Filled.AccountCircle,
                    contentDescription = ""
                )
            }
        }
    )
}

@Composable
fun UserInputField() {
    val value = remember {
        mutableStateOf(TextFieldValue(""))
    }
    Row(
        modifier = Modifier
            .padding(start = 8.dp, end = 8.dp, top = 8.dp, bottom = 16.dp)
            .fillMaxWidth()
    ) {
        IconButton(
            onClick = { /*TODO*/ },
            modifier = Modifier
                .size(48.dp)
                .align(Alignment.CenterVertically)
        ) {
            Icon(
                imageVector = Icons.Filled.AttachFile,
                contentDescription = ""
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        Surface(
            shadowElevation = 4.dp,
            shape = RoundedCornerShape(15.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        when {
                            isSystemInDarkTheme() -> Gray70
                            else -> Gray30
                        },
                        RoundedCornerShape(15.dp)
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                BasicTextField(
                    value = value.value,
                    onValueChange = { value.value = it },
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 16.dp, end = 8.dp),
                    maxLines = 4,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next
                    ),
                    cursorBrush = SolidColor(MaterialTheme.colorScheme.onBackground),
                    textStyle = TextStyle(
                        color = MaterialTheme.colorScheme.onBackground,
                        fontFamily = museoRegular,
                        fontSize = 16.sp
                    ),
                    decorationBox = { innerTextField ->
                        Box {
                            if (value.value.text.isEmpty()) {
                                Text(
                                    text = "Message",
                                    color = MaterialTheme.colorScheme.onBackground,
                                    modifier = Modifier
                                        .alpha(0.5f),
                                    fontFamily = museoRegular
                                )
                            }
                            innerTextField()
                        }
                    }

                )
                Column(
                    modifier = Modifier
                        .padding(vertical = 4.dp, horizontal = 5.dp)
                        .size(40.dp)
                        .background(MaterialTheme.colorScheme.background, RoundedCornerShape(12.dp)),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Outlined.SendOutlined,
                        contentDescription = "",
                        modifier = Modifier
                            .size(24.dp)
                            .align(Alignment.CenterHorizontally)
                    )
                }
            }

        }
    }
}
