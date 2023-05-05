package com.ikuzMirel.flick.ui.friends

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.AttachFile
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ikuzMirel.flick.R
import com.ikuzMirel.flick.ui.components.icons.SendOutlined
import com.ikuzMirel.flick.ui.components.topAppBars.TitleAvatarTopBar
import com.ikuzMirel.flick.ui.destinations.ChatDestination
import com.ikuzMirel.flick.ui.extension.noRippleClickable
import com.ikuzMirel.flick.ui.mainContent.contact.ContactListItem
import com.ikuzMirel.flick.ui.mainContent.contact.ContactViewModel
import com.ikuzMirel.flick.ui.mainContent.contact.contactList
import com.ikuzMirel.flick.ui.theme.*
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Destination
@Composable
fun AddFriend(
    navigator: DestinationsNavigator,
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        TitleAvatarTopBar(navigator, "Add friends")
    }
}

@Composable
fun UserInputField() {
    val value = remember {
        mutableStateOf(TextFieldValue(""))
    }
    Row(
        modifier = Modifier
            .padding(start = 33.dp, end = 33.dp, top = 8.dp, bottom = 16.dp)
            .width(294.dp)

    ) {
        Surface(
            shadowElevation = 4.dp,
            shape = RoundedCornerShape(15.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        when {
                            isSystemInDarkTheme() -> Gray70
                            else -> Gray30
                        },
                        RoundedCornerShape(15.dp)
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                BasicTextField(
                    value = value.value,
                    onValueChange = { value.value = it },
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 16.dp, end = 8.dp),
                    maxLines = 4,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next
                    ),
                    cursorBrush = SolidColor(MaterialTheme.colorScheme.onBackground),
                    textStyle = TextStyle(
                        color = MaterialTheme.colorScheme.onBackground,
                        fontFamily = museoRegular,
                        fontSize = 16.sp
                    ),
                    decorationBox = { innerTextField ->
                        Box {
                            if (value.value.text.isEmpty()) {
                                Text(
                                    text = "Find",
                                    color = MaterialTheme.colorScheme.onBackground,
                                    modifier = Modifier
                                        .alpha(0.5f),
                                    fontFamily = museoRegular
                                )
                            }
                            innerTextField()
                        }
                    }
                )
                Column(
                    modifier = Modifier
                        .padding(vertical = 4.dp, horizontal = 5.dp)
                        .size(40.dp)
                        .background(
                            MaterialTheme.colorScheme.background,
                            RoundedCornerShape(12.dp)
                        ),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Search,
                        contentDescription = "",
                        modifier = Modifier
                            .size(24.dp)
                            .align(Alignment.CenterHorizontally)
                    )
                }
            }
        }
    }
}

@Composable
fun NewFriend(
    name: String,
    //avatar:
) {
    Box(
        modifier = Modifier
            .padding(top = 26.dp, start = 32.dp),
    ) {
        Surface(
            shadowElevation = 16.dp,
            shape = RoundedCornerShape(
                topEnd = 18.dp,
                bottomEnd = 18.dp,
                topStart = 32.dp,
                bottomStart = 32.dp
            ),
        ) {
            Row(
                modifier = Modifier
                    .width(292.dp)
                    .height(64.dp)
                    .padding(start = 32.dp)
                    .background(
                        color = Gray90,
                        RoundedCornerShape(topEnd = 18.dp, bottomEnd = 18.dp)
                    ),

                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .padding(start = 42.dp)
                        .weight(1f),

                    text = name,
                    fontSize = 28.sp,
                    fontFamily = museoRegular,
                    color = MaterialTheme.colorScheme.onBackground,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1
                )
                Column(
                    modifier = Modifier
                        .padding(end = 12.dp)
                        .size(48.dp)
                        .background(
                            Color.White,
                            RoundedCornerShape(12.dp)
                        )
                        .noRippleClickable {
                            // TODO:
                        },
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        tint = Gray80,
                        imageVector = Icons.Outlined.Add,
                        contentDescription = "",
                        modifier = Modifier
                            .size(36.dp)
                            .align(Alignment.CenterHorizontally)
                    )
                }
            }
        }
        Image(
            painter = painterResource(id = R.drawable.avatar),
            contentDescription = "Avatar",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(64.dp)
                .clip(CircleShape)
        )
    }
}