package com.ikuzMirel.flick.ui.components.listItem

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Block
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.ikuzMirel.flick.R
import com.ikuzMirel.flick.ui.components.button.SimpleLargeButton
import com.ikuzMirel.flick.ui.theme.Blue50
import com.ikuzMirel.flick.ui.theme.Gray90
import com.ikuzMirel.flick.ui.theme.Green50
import com.ikuzMirel.flick.ui.theme.Red50
import com.ikuzMirel.flick.ui.theme.museoRegular

enum class FriendRequestItemType {
    Add,
    Friend,
    Received,
    Sent,
    None
}

@Composable
fun FriendReqListItem(
    name: String,
    onClick: () -> Unit = {},
    onSecClick: () -> Unit = {},
    type: FriendRequestItemType,
    infoText: String = ""
    //avatar:
) {
    Box(
        Modifier.padding(vertical = 8.dp)
    ){
        Surface(
            color = Gray90,
            shape = RoundedCornerShape(
                topEnd = 18.dp,
                bottomEnd = 18.dp
            ),
            modifier = Modifier.padding(start = 32.dp),
            shadowElevation = 4.dp,
        ) {
            Row(
                modifier = Modifier
                    .height(64.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .padding(start = 42.dp)
                        .weight(1f),
                    text = name,
                    style = MaterialTheme.typography.headlineSmall,
                    fontFamily = museoRegular,
                    color = MaterialTheme.colorScheme.onBackground,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1
                )
                when (type) {
                    FriendRequestItemType.Add -> {
                        SimpleLargeButton(
                            onClick = onClick,
                            icon = Icons.Filled.Add,
                            tint = Green50
                        )
                    }
                    FriendRequestItemType.Friend -> {
                        SimpleLargeButton(
                            onClick = onClick,
                            icon = Icons.Filled.Send
                        )
                    }
                    FriendRequestItemType.Received -> {
                        Row() {
                            SimpleLargeButton(
                                onClick = onClick,
                                icon = Icons.Filled.Done,
                                tint = Blue50
                            )
                            SimpleLargeButton(
                                onClick = onSecClick,
                                icon = Icons.Filled.Block,
                                tint = Red50
                            )
                        }
                    }
                    FriendRequestItemType.Sent -> {
                        SimpleLargeButton(
                            onClick = onClick,
                            icon = Icons.Filled.Close,
                            tint = Red50
                        )
                    }
                    FriendRequestItemType.None -> {
                        Text(
                            modifier = Modifier.padding(end = 16.dp),
                            text = infoText,
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }
                }
            }
        }
        Image(
            painter = painterResource(id = R.drawable.avatar),
            contentDescription = "Avatar",
            modifier = Modifier.size(64.dp)
        )
    }
}