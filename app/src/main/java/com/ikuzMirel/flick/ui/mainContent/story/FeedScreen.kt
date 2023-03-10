package com.ikuzMirel.flick.ui.mainContent.story

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ikuzMirel.flick.R
import com.ikuzMirel.flick.ui.theme.cocogooseLight

// TODO: Hardcoded data source, change when viewModel is created

@Composable
fun Feed() {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 64.dp)
    ){
        items(5){
            FeedItem(
                user = "Xuan",
                game = "Valorant",
                isLikeClicked = {},
                isCommentClicked = {}
            )
        }
    }
}

@Composable
fun FeedItem(
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