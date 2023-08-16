package com.ikuzMirel.flick.ui.chat

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.InfiniteTransition
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.AttachFile
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ikuzMirel.flick.R
import com.ikuzMirel.flick.domain.model.Message
import com.ikuzMirel.flick.domain.model.MessageState
import com.ikuzMirel.flick.ui.components.icons.SendOutlined
import com.ikuzMirel.flick.ui.components.textFields.UserInputTextField
import com.ikuzMirel.flick.ui.extension.noRippleClickable
import com.ikuzMirel.flick.ui.theme.*
import com.ramcosta.composedestinations.annotation.DeepLink
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Destination(
    deepLinks = [
        DeepLink(
            uriPattern = "https://flick.com/chat/{username}/{userId}/{collectionId}",
        )
    ]
)
@Composable
fun Chat(
    navigator: DestinationsNavigator,
    viewModel: ChatViewModel = hiltViewModel(),
    username: String,
    userId: String,
    collectionId: String,
) {
    val focusManager = LocalFocusManager.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val scrollState = rememberLazyListState()
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val infiniteTransition = rememberInfiniteTransition()

    DisposableEffect(key1 = lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_START) {
                viewModel.getChatMessages(collectionId)
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

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
                    viewModel = viewModel,
                    messages = state.messages,
                    modifier = Modifier
                        .weight(1f),
                    scrollState = scrollState,
                    state = state,
                    infiniteTransition = infiniteTransition,
                    collectionId = collectionId,
                    receiverId = userId,
                )
                ChatBottomBar(viewModel, state, collectionId, userId)
            }
            TitleAvatarTopBar(navigator, username, userId)
        }
    }
}

@Composable
private fun Content(
    viewModel: ChatViewModel,
    messages: List<Message>,
    modifier: Modifier,
    scrollState: LazyListState,
    state: ChatUIState,
    infiniteTransition: InfiniteTransition,
    collectionId: String,
    receiverId: String,
) {
    val pulseAnim by infiniteTransition.animateFloat(
        initialValue = 0.8f,
        targetValue = 0.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1000
            ),
            RepeatMode.Reverse
        )
    )

    Box(modifier = modifier) {

        LazyColumn(
            modifier = Modifier.wrapContentSize(),
            state = scrollState,
            reverseLayout = true,
            contentPadding = WindowInsets.statusBars.add(WindowInsets(top = 76.dp))
                .asPaddingValues()
        ) {
            items(messages) {
                LaunchedEffect(true){
                    if (it.unread) {
                        viewModel.markMessageAsRead(it.id)
                    }
                }

                Message(
                    viewModel = viewModel,
                    userMe = state.senderId,
                    message = it,
                    transition = pulseAnim,
                    collectionId = collectionId,
                    receiverId = receiverId
                )
            }
        }

    }
}

@Composable
fun Message(
    viewModel: ChatViewModel,
    collectionId: String,
    receiverId: String,
    userMe: String,
    message: Message,
    transition: Float
) {
    val isOwnMessage = message.userId == userMe
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
                modifier = when (message.state) {
                    MessageState.SENDING.name -> Modifier
                        .alpha(transition)
                        .background(Purple50, RoundedCornerShape(12.dp))
                    MessageState.SENT.name -> Modifier
                        .background(Purple50, RoundedCornerShape(12.dp))
                        .noRippleClickable { setShowTime(!showTime) }
                        .animateContentSize()
                    MessageState.ERROR.name -> Modifier
                        .background(Purple50, RoundedCornerShape(12.dp))
                        .border(2.dp, Red70, RoundedCornerShape(12.dp))
                        .noRippleClickable {
                            viewModel.resendMessage(
                                message,
                                collectionId,
                                receiverId
                            )
                        }
                    else -> Modifier
                }
            ) {
                Text(
                    text = message.content,
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
                        text = message.timestamp,
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
                    text = message.content,
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
                        text = message.timestamp,
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
    navigator: DestinationsNavigator,
    username: String,
    userId: String,
) {
    CenterAlignedTopAppBar(
        navigationIcon = {
            IconButton(
                onClick = { navigator.popBackStack() }
            ) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = ""
                )
            }
        },
        title = {
            Text(
                text = username,
                fontFamily = museoRegular
            )
        },
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
fun ChatBottomBar(
    viewModel: ChatViewModel,
    state: ChatUIState,
    collectionId: String,
    receiverId: String
) {
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
        UserInputTextField(
            textFieldValue = state.message,
            onTextFieldValueChange = { viewModel.onMessageChange(it) },
            maxLines = 4,
            hintText = "Say something...",
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next
            ),
            actionIcon = Icons.Outlined.SendOutlined,
            onActionClick = {
                viewModel.sendMessage(
                    state.message,
                    collectionId,
                    receiverId
                )
            }
        )
    }
}
