package com.ikuzMirel.flick.ui.home

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.PowerManager
import android.provider.Settings
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material.icons.rounded.BatteryAlert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.ikuzMirel.flick.ui.components.dialogs.BasicDialog
import com.ikuzMirel.flick.ui.destinations.AddFriendDestination
import com.ikuzMirel.flick.ui.destinations.SettingDestination
import com.ikuzMirel.flick.ui.home.contact.Contact
import com.ikuzMirel.flick.ui.home.feed.Feed
import com.ikuzMirel.flick.ui.home.map.Map
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.launch

enum class Screens(val icon: ImageVector, val contentDescription: String) {
    Map(icon = Icons.Outlined.Public, contentDescription = "Map"),
    Contacts(icon = Icons.Outlined.Contacts, contentDescription = "Contacts"),
    Feed(icon = Icons.Outlined.Forum, contentDescription = "Feed")
}

@RootNavGraph(start = true)
@OptIn(ExperimentalFoundationApi::class)
@Destination
@Composable
fun Home(
    navigator: DestinationsNavigator
) {
    val pagerState = rememberPagerState(
        initialPage = 1
    )
    val context = LocalContext.current
    val powerManage by lazy {
        context.getSystemService(Context.POWER_SERVICE) as PowerManager
    }
    var showDialog by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(Unit){
        if(!powerManage.isIgnoringBatteryOptimizations(context.packageName)) {
            showDialog = true
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        if(showDialog){
            BasicDialog(
                icon = Icons.Rounded.BatteryAlert,
                title = "Better experience",
                content = "To ensure this app works properly, we recommend disabling battery optimization. Battery optimization can close the app in the background to save power, but it may prevent the app from updating information or delivering notifications on time.",
                dismissBtnText = "No, thant's ok",
                confirmBtnText = "Yes, I will do it",
                dismissBtnAction = {
                    showDialog = false
                },
                confirmBtnAction = {
                    val intent = Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS)
                    intent.data = Uri.parse("package:com.ikuzMirel.flick")
                    context.startActivity(intent)
                    showDialog = false
                }
            )
        }

        Box(
            modifier = Modifier
                .fillMaxSize(),
        ) {
            Content(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 80.dp),
                pagerState = pagerState,
                navigator
            )
            HomeTopAppBar(navigator)
            UseNavigationBar(
                modifier = Modifier.align(Alignment.BottomCenter),
                pagerState = pagerState
            )
        }

    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun Content(
    modifier: Modifier,
    pagerState: PagerState,
    navigator: DestinationsNavigator
) {
    HorizontalPager(
        state = pagerState,
        modifier = modifier,
        userScrollEnabled = when (pagerState.currentPage) {
            0 -> false
            else -> true
        },
        pageCount = Screens.values().size
    ) { index ->
        when (index) {
            0 -> Map()
            1 -> Contact(navigator)
            2 -> Feed()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeTopAppBar(
    navigator: DestinationsNavigator
) {
    TopAppBar(
        title = {},
        navigationIcon = {
            IconButton(onClick = { /* TODO: PROFILE */ }) {
                Icon(
                    imageVector = Icons.Outlined.AccountCircle,
                    contentDescription = "Account"
                )
            }
        },
        actions = {
            IconButton(onClick = { navigator.navigate(AddFriendDestination) }) {
                Icon(
                    imageVector = Icons.Outlined.Search,
                    contentDescription = "Add friend"
                )
            }
            IconButton(onClick = { navigator.navigate(SettingDestination) }) {
                Icon(
                    imageVector = Icons.Outlined.Settings,
                    contentDescription = "Settings"
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent
        )
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun UseNavigationBar(modifier: Modifier, pagerState: PagerState) {
    val scope = rememberCoroutineScope()
    NavigationBar(
        modifier = modifier,
        containerColor = Color.Transparent
    ) {
        Screens.values().forEachIndexed { index, screen ->
            NavigationBarItem(
                icon = {
                    Icon(
                        imageVector = screen.icon,
                        contentDescription = screen.contentDescription,
                        modifier = Modifier.size(24.dp)
                    )
                },
                selected = pagerState.currentPage == index,
                onClick = {
                    scope.launch {
                        pagerState.animateScrollToPage(index)
                    }
                }
            )
        }
    }
}