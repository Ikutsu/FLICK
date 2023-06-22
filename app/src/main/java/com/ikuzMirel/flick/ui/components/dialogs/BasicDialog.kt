package com.ikuzMirel.flick.ui.components.dialogs

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.End
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties

//A basic Material3 Dialog
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BasicDialog(
    onDismissRequest: () -> Unit = {},
    icon: ImageVector,
    title: String,
    content: String,
    dismissBtnText: String,
    dismissBtnAction: () -> Unit = {},
    confirmBtnText: String,
    confirmBtnAction: () -> Unit = {}
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(
            dismissOnBackPress = false,
            dismissOnClickOutside = false
        ),
        modifier = Modifier
            .clip(RoundedCornerShape(28.dp)),
    ){
        Surface() {
            Column(
                modifier = Modifier
                    .padding(24.dp)
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = "",
                    modifier = Modifier
                        .size(24.dp)
                        .align(CenterHorizontally)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = title,
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier
                        .align(CenterHorizontally)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = content,
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.height(24.dp))
                Row(
                    modifier = Modifier
                        .align(End)
                ) {
                    TextButton(onClick = dismissBtnAction) {
                        Text(
                            text = dismissBtnText,
                            style = MaterialTheme.typography.labelLarge
                        )
                    }
                    Spacer(
                        modifier = Modifier
                            .width(8.dp)
                    )
                    TextButton(onClick = confirmBtnAction) {
                        Text(
                            text = confirmBtnText,
                            style = MaterialTheme.typography.labelLarge
                        )
                    }
                }
            }
        }
    }
}