package com.ikuzMirel.flick.ui.searchFriend

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.PersonAddAlt
import androidx.compose.material.icons.outlined.QuestionMark
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ikuzMirel.flick.data.response.BasicResponse
import com.ikuzMirel.flick.ui.components.emptyState.SimpleEmptyStateScreen
import com.ikuzMirel.flick.ui.components.listItem.FriendReqListItem
import com.ikuzMirel.flick.ui.components.listItem.FriendRequestItemType
import com.ikuzMirel.flick.ui.components.textFields.UserInputTextField
import com.ikuzMirel.flick.ui.destinations.ChatDestination
import com.ikuzMirel.flick.ui.destinations.FriendRequestDestination
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@OptIn(ExperimentalMaterial3Api::class)
@Destination
@Composable
fun AddFriend(
    navigator: DestinationsNavigator,
    viewModel: SearchFriendViewModel = hiltViewModel()
) {
    val focusManager = LocalFocusManager.current
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    LaunchedEffect(state, context){
        viewModel.actionResult.collect {
            when(it){
                is BasicResponse.Success -> {
                    snackbarHostState.showSnackbar(it.data!!)
                }
                is BasicResponse.Error -> {
                    snackbarHostState.showSnackbar(it.errorMessage)
                }
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(text = "Add Friend")
                },
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
                actions = {
                    IconButton(onClick = { navigator.navigate(FriendRequestDestination) }) {
                        Icon(
                            imageVector = Icons.Outlined.PersonAddAlt,
                            contentDescription = ""
                        )
                    }
                }
            )
        },
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures(onTap = {
                    focusManager.clearFocus()
                })
            }
    ) {
        Column(
            modifier = Modifier.padding(
                start = 32.dp,
                end = 32.dp,
                top = it.calculateTopPadding()
            )
        ) {
            UserInputTextField(
                textFieldValue = state.searchQuery,
                onTextFieldValueChange = { viewModel.onSearchQueryChanged(it) },
                hintText = "Search for a friend",
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Search
                ),
                keyboardActions = KeyboardActions(
                    onSearch = {
                        focusManager.clearFocus()
                    }
                ),
                actionIcon = Icons.Outlined.Search,
                onActionClick = {},
            )
            Box {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    items(state.searchResults) { result ->
                        FriendReqListItem(
                            name = result.username,
                            onClick = {
                                when (result.friendWithMe) {
                                    true -> {
                                        navigator.navigate(
                                            ChatDestination(
                                                result.username,
                                                result.userId,
                                                result.collectionId
                                            )
                                        )
                                    }

                                    false -> {
                                        viewModel.onAddFriendClicked(result.userId)
                                    }
                                }
                            },
                            type = if (result.friendWithMe) {
                                FriendRequestItemType.Friend
                            } else {
                                FriendRequestItemType.Add
                            }
                        )
                    }
                }
                if (state.showEmptyState) {
                    SimpleEmptyStateScreen(
                        icon = Icons.Outlined.QuestionMark,
                        title = "Couldn't Find User :(",
                        description = "The user you were searching for doesn't exist. Please check the name again and try searching.",
                        buttonShow = false,
                        descriptionModifier = Modifier.padding(horizontal = 32.dp)
                    )
                }
            }
        }
    }
}

