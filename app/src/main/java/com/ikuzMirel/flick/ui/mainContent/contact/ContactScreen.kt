package com.ikuzMirel.flick.ui.mainContent.contact

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ikuzMirel.flick.R
import com.ikuzMirel.flick.data.model.ContactModel
import com.ikuzMirel.flick.ui.destinations.ChatDestination
import com.ikuzMirel.flick.ui.theme.*
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Composable
fun Contact(
    navigator: DestinationsNavigator
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 64.dp)
    ) {

        items(contactList) {
            ContactListItem(it, navigator)
        }
    }
}

// TODO: Image for avatar is temporary placeholder
@Composable
fun ContactListItem(
    contactModel: ContactModel,
    navigator: DestinationsNavigator
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {navigator.navigate(ChatDestination)},

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
                        contactModel.status
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
                text = contactModel.name,
                fontSize = 16.sp,
                fontFamily = cocogooseBold,
                color = MaterialTheme.colorScheme.onBackground
            )

            Text(
                text = contactModel.latestMessage,
                fontSize = 12.sp,
                fontFamily = cocogooseLight,
                color = Color.Gray
            )
        }

        if (contactModel.notification != 0) {
            Column(
                modifier = Modifier
                    .padding(
                        end = 18.dp
                    )
                    .background(
                        color = Color.Red,
                        shape = when {
                            contactModel.notification > 9 -> RoundedCornerShape(50)
                            else -> RoundedCornerShape(100)
                        }
                    ),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally

            ) {
                Text(
                    text = when {
                        contactModel.notification > 99 -> "99+"
                        else -> contactModel.notification.toString()
                    },
                    fontSize = 12.sp,
                    color = Color.White,
                    modifier = Modifier
                        .padding(horizontal = 4.dp)
                )
            }
        }
    }
}