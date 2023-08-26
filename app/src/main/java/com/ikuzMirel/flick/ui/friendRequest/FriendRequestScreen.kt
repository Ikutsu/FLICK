package com.ikuzMirel.flick.ui.friendRequest

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.with
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ikuzMirel.flick.data.response.BasicResponse
import com.ikuzMirel.flick.domain.entities.FriendRequestStatus
import com.ikuzMirel.flick.ui.components.listItem.FriendReqListItem
import com.ikuzMirel.flick.ui.components.listItem.FriendRequestItemType
import com.ikuzMirel.flick.ui.destinations.ChatDestination
import com.ikuzMirel.flick.ui.theme.Blue50
import com.ikuzMirel.flick.ui.theme.Gray50
import com.ikuzMirel.flick.utils.capitalizeFirstLetter
import com.ramcosta.composedestinations.annotation.DeepLink
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

enum class FriendRequestType {
    Received,
    Sent
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Destination(
    deepLinks = [
        DeepLink(
            uriPattern = "https://flick.com/FriendRequest",
        )
    ]
)
@Composable
fun FriendRequest(
    navigator: DestinationsNavigator
) {
    val viewModel: FriendRequestViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember {
        SnackbarHostState()
    }
    val pagerState = rememberPagerState()
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    LaunchedEffect(uiState, context) {
        viewModel.requestResult.collect {
            when (it) {
                is BasicResponse.Success -> {
                    snackbarHostState.showSnackbar(
                        message = it.data!!
                    )
                }

                is BasicResponse.Error -> {
                    snackbarHostState.showSnackbar(
                        message = it.errorMessage
                    )
                }
            }
        }
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState,
            )
        },
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(text = "Friend Requests")
                },
                navigationIcon = {
                    IconButton(onClick = { navigator.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = ""
                        )
                    }
                }
            )
        },
    ) { paddingValues ->
        Column(
            Modifier.padding(paddingValues)
        ) {
            TabRow(
                selectedTabIndex = pagerState.currentPage,
                indicator = {
                    Box(
                        modifier = Modifier
                            .tabIndicatorOffset(it[pagerState.currentPage])
                            .height(5.dp)
                            .padding(horizontal = 42.dp)
                            .background(Blue50, RoundedCornerShape(10.dp))
                    )
                },
                divider = {},
                modifier = Modifier
                    .padding(horizontal = 72.dp)
            ) {
                FriendRequestType.values().forEachIndexed { index, abc ->
                    Tab(
                        selected = pagerState.currentPage == index,
                        onClick = {
                            scope.launch {
                                pagerState.animateScrollToPage(index)
                            }
                        },
                        modifier = Modifier
                            .height(40.dp)
                            .width(108.dp),
                        selectedContentColor = Color.White,
                        unselectedContentColor = Gray50
                    ) {
                        Text(text = abc.name)
                    }
                }
            }
            HorizontalPager(
                state = pagerState,
                pageCount = FriendRequestType.values().size
            ) { index ->
                when (index) {
                    0 -> ReceivedScreen(uiState, viewModel, navigator)
                    1 -> SentScreen(uiState, viewModel)
                }
            }
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun ReceivedScreen(
    state: FriendRequestUIState,
    viewModel: FriendRequestViewModel,
    navigator: DestinationsNavigator
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 8.dp, start = 32.dp, end = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        items(state.receivedRequests) {
            AnimatedContent(
                targetState = it.status,
                transitionSpec = {
                    fadeIn(tween(200)) with fadeOut()
                }
            ) { status ->
                when (status) {
                    FriendRequestStatus.ACCEPTED.name -> {
                        FriendReqListItem(
                            name = it.senderName,
                            onClick = {
                                runBlocking {
                                    navigator.navigate(
                                        ChatDestination(
                                            it.senderName,
                                            it.senderId,
                                            viewModel.getCollectionId(it.senderId)
                                        )
                                    )
                                }
                            },
                            type = FriendRequestItemType.Friend
                        )
                    }

                    FriendRequestStatus.REJECTED.name -> {
                        FriendReqListItem(
                            name = it.senderName,
                            type = FriendRequestItemType.None,
                            infoText = FriendRequestStatus.REJECTED.name.capitalizeFirstLetter()
                        )
                    }

                    FriendRequestStatus.PENDING.name -> {
                        FriendReqListItem(
                            name = it.senderName,
                            onClick = { viewModel.onAcceptRequest(it.id) },
                            onSecClick = { viewModel.onRejectRequest(it.id) },
                            type = FriendRequestItemType.Received
                        )
                    }
                }

            }
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun SentScreen(
    state: FriendRequestUIState,
    viewModel: FriendRequestViewModel
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 8.dp, start = 32.dp, end = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        items(state.sentRequests) {
            AnimatedContent(
                targetState = it.status,
                transitionSpec = {
                    fadeIn(tween(200)) with fadeOut()
                }
            ) { status ->
                when (status) {
                    FriendRequestStatus.CANCELED.name -> {
                        FriendReqListItem(
                            name = it.receiverName,
                            type = FriendRequestItemType.None,
                            infoText = FriendRequestStatus.CANCELED.name.capitalizeFirstLetter()
                        )
                    }

                    FriendRequestStatus.REJECTED.name -> {
                        FriendReqListItem(
                            name = it.receiverName,
                            type = FriendRequestItemType.None,
                            infoText = FriendRequestStatus.REJECTED.name.capitalizeFirstLetter()
                        )
                    }

                    FriendRequestStatus.ACCEPTED.name -> {
                        FriendReqListItem(
                            name = it.receiverName,
                            type = FriendRequestItemType.None,
                            infoText = FriendRequestStatus.ACCEPTED.name.capitalizeFirstLetter()
                        )
                    }

                    FriendRequestStatus.PENDING.name -> {
                        FriendReqListItem(
                            name = it.receiverName,
                            onClick = { viewModel.onCancelRequest(it.id) },
                            type = FriendRequestItemType.Sent
                        )
                    }
                }
            }
        }
    }
}