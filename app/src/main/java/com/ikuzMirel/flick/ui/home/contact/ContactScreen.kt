package com.ikuzMirel.flick.ui.home.contact

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ikuzMirel.flick.R
import com.ikuzMirel.flick.domain.model.Friend
import com.ikuzMirel.flick.ui.destinations.ChatDestination
import com.ikuzMirel.flick.ui.theme.Amber40
import com.ikuzMirel.flick.ui.theme.Green70
import com.ikuzMirel.flick.ui.theme.Red70
import com.ikuzMirel.flick.ui.theme.museoRegular
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Composable
fun Contact(
    navigator: DestinationsNavigator,
    viewModel: ContactViewModel = hiltViewModel()
) {
    val state by viewModel.state

    LaunchedEffect(Unit){
        viewModel.getFriends()
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 64.dp)
    ) {

        items(state.friends) {
            ContactListItem(it, navigator)
        }
    }
}

// TODO: Image for avatar is temporary placeholder
@Composable
fun ContactListItem(
    friend: Friend,
    navigator: DestinationsNavigator
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                navigator.navigate(
                    ChatDestination(
                        friend.name,
                        friend.userId,
                        friend.collectionId
                    )
                )
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        
        Box (
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .padding(
                    start = 8.dp,
                    top = 4.dp,
                    bottom = 4.dp
        )
        ){
            Canvas(
                modifier = Modifier
                    .size(54.dp)
            ) {
                drawCircle(
                    color = when (
                        friend.status
                    ){
                        0 -> Green70
                        1 -> Amber40
                        2 -> Color.Gray
                        3 -> Red70
                        else -> Color.Transparent
                    },
                    style = Stroke(5F)
                )
            }
            Image(
                painter = painterResource(id = R.drawable.avatar),
                contentDescription = "Avatar",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
            )
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(
                    start = 11.dp
                )
        ) {
            Text(
                text = friend.name,
                fontSize = 16.sp,
                fontFamily = museoRegular,
                color = MaterialTheme.colorScheme.onBackground
            )

            Text(
                text = friend.latestMessage,
                fontSize = 12.sp,
                fontFamily = museoRegular,
                color = Color.Gray
            )
        }

        if (friend.notification != 0) {
            Column(
                modifier = Modifier
                    .padding(
                        end = 18.dp
                    )
                    .background(
                        color = Color.Red,
                        shape = when {
                            friend.notification > 9 -> RoundedCornerShape(50)
                            else -> RoundedCornerShape(100)
                        }
                    ),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally

            ) {
                Text(
                    text = when {
                        friend.notification > 99 -> "99+"
                        else -> friend.notification.toString()
                    },
                    fontSize = 12.sp,
                    color = Color.White,
                    fontFamily = museoRegular,
                    modifier = Modifier
                        .padding(horizontal = 4.dp)
                )
            }
        }
    }
}