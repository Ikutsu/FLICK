package com.ikuzMirel.flick.ui.mainContent

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState
import com.ikuzMirel.flick.ui.mainContent.contact.Contact
import com.ikuzMirel.flick.ui.mainContent.map.Map
import com.ikuzMirel.flick.ui.mainContent.story.Feed
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.launch

// TODO: Bad name
enum class Screens(val icon: ImageVector, val contentDescription: String) {
    Map(icon = Icons.Outlined.Public, contentDescription = "Map"),
    Contacts(icon = Icons.Outlined.Contacts, contentDescription = "Contacts"),
    Feed(icon = Icons.Outlined.Forum, contentDescription = "Feed")
}

// TODO: Bad name
@Destination
@OptIn(ExperimentalPagerApi::class)
@Composable
fun MainContent(
    navigator: DestinationsNavigator
) {
    val pagerState = rememberPagerState(initialPage = 1)

    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
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
            UseTopAppBar()
            UseNavigationBar(
                modifier = Modifier.align(Alignment.BottomCenter),
                pagerState = pagerState
            )
        }

    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
private fun Content(
    modifier: Modifier,
    pagerState: PagerState,
    navigator: DestinationsNavigator
) {
    HorizontalPager(
        state = pagerState,
        count = Screens.values().size,
        modifier = modifier,
        userScrollEnabled = when (pagerState.currentPage) {
            0 -> false
            else -> true
        }
    ) { index ->
        when (index) {
            0 -> Map()
            1 -> Contact(navigator)
            2 -> Feed()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPagerApi::class)
@Composable
fun UseTopAppBar() {
    TopAppBar(
        title = {},
        navigationIcon = {
            IconButton(onClick = { /* doSomething() */ }) {
                Icon(
                    imageVector = Icons.Outlined.AccountCircle,
                    contentDescription = "Account"
                )
            }
        },
        actions = {
            IconButton(onClick = { /* doSomething() */ }) {
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

@OptIn(ExperimentalPagerApi::class)
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