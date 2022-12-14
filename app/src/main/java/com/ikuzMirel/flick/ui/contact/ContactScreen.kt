package com.ikuzMirel.flick.ui.contact

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.ui.tooling.preview.Preview
import com.ikuzMirel.flick.ui.theme.FLICKTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.sp
import com.ikuzMirel.flick.R

enum class Screens(val icon: ImageVector, val contentDescription: String) {
    Map(icon = Icons.Outlined.Public, contentDescription = "Map"),
    Contacts(icon = Icons.Outlined.Contacts, contentDescription = "Contacts"),
    Feed(icon = Icons.Outlined.Forum, contentDescription = "Feed")

}

@Preview(showBackground = true)
@Composable
fun Preview() {
    FLICKTheme(
        darkTheme = true
    ) {
        Contact()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Contact() {
    Scaffold(
        topBar = { UseTopAppBar() },

        bottomBar = { UseNavigationBar() }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {

            items(contactList) {
                Friend(it)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
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
        }

    )
}

@Composable
fun UseNavigationBar() {
    var selectedItem by remember { mutableStateOf(1) }

    NavigationBar(
        containerColor = Color.Transparent
    ) {
        Screens.values().forEachIndexed { index, screen ->
            NavigationBarItem(
                icon = {
                    Icon(
                        screen.icon,
                        contentDescription = screen.contentDescription,
                        modifier = Modifier.size(24.dp)
                    )
                },
                selected = selectedItem == index,
                onClick = { selectedItem = index }
            )
        }
    }
}

// TODO: Image for avatar is temporary placeholder 
@Composable
fun Friend(
    friendmodel: FriendModel
) {
    val cocogoose = FontFamily(
        Font(R.font.cocogoose)
    )

    val cocogooseUltralight = FontFamily(
        Font(R.font.cocogoose_ultralight)
    )

    val roboto = FontFamily(
        Font(R.font.roboto_condensed_regular)
    )

    Row(
        modifier = Modifier
            .fillMaxWidth(),

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
                        friendmodel.status
                    ){
                        0 -> Color.Green
                        1 -> Color.Yellow
                        2 -> Color.Gray
                        3 -> Color.Red
                        else -> Color.Transparent
                    },
                    style = Stroke(5F)
                )
            }
            
            
            Image(
                painter = painterResource(id = R.drawable.placeholder),
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
                text = friendmodel.name,
                fontSize = 16.sp,
                fontFamily = cocogoose,
                color = Color.White
            )

            Text(
                text = friendmodel.latestMessage,
                fontSize = 12.sp,
                fontFamily = cocogooseUltralight,
                color = Color.Gray
            )
        }

        if (friendmodel.notification != 0) {
            Column(
                modifier = Modifier
                    .padding(
                        end = 18.dp
                    )
                    .background(
                        color = Color.Red,
                        shape = when {
                            friendmodel.notification > 9 -> RoundedCornerShape(50)
                            else -> RoundedCornerShape(100)
                        }
                    ),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally

            ) {
                Text(
                    text = when {
                        friendmodel.notification > 99 -> "99+"
                        else -> friendmodel.notification.toString()
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