package com.ikuzMirel.flick.ui.story

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ikuzMirel.flick.R
import com.ikuzMirel.flick.ui.components.topAppBars.NavOnlyTopBar
import com.ikuzMirel.flick.ui.theme.cocogooseLight
import kotlin.random.Random

// TODO: Find a better name for the screen
// TODO: Hardcoded data source, change when viewModel is created
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Story() {
    Scaffold(
        topBar = { NavOnlyTopBar {} }, //TODO: Change when chat screen is done
        bottomBar = { NavigationBar() {} }, //TODO: Change when chat screen is done
        modifier = Modifier
            .fillMaxSize()
    ) { paddingValues ->
        Content(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        )
    }
}

@Composable
private fun Content(
    modifier: Modifier,
) {

    LazyColumn(
        modifier = modifier
    ){
        items(5){
            StoryItem(
                user = "Xuan",
                game = "Valorant",
                isLikeClicked = {},
                isCommentClicked = {}
            )
        }
    }
}

@Composable
fun StoryItem(
    user: String,
    game: String,
    isLikeClicked: () -> Unit,
    isCommentClicked: () -> Unit
) {
    val (isLikedState, setLiked) = remember {
        mutableStateOf(false)
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp)
                .height(48.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.avatar),
                contentDescription = "",
                modifier = Modifier
                    .size(32.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "$user - In $game",
                color = MaterialTheme.colorScheme.onBackground,
                fontFamily = cocogooseLight,
                fontSize = 14.sp
            )
        }
        Image(
            painter = painterResource(id = R.drawable.placeholder),
            contentDescription = "",
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconToggleButton(checked = isLikedState, onCheckedChange = { setLiked(!isLikedState) }) {
                Icon(
                    imageVector = if (isLikedState) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                    contentDescription = "",
                    tint = if (isLikedState) Color.Red else MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.size(28.dp)
                )
            }
            IconToggleButton(checked = false, onCheckedChange = { isCommentClicked() }) {
                Icon(
                    imageVector = Icons.Outlined.ChatBubbleOutline,
                    contentDescription = "",
                    tint = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.size(28.dp)
                )
            }
        }
    }
}