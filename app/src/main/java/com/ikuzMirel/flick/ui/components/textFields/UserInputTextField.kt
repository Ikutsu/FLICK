package com.ikuzMirel.flick.ui.components.textFields

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ikuzMirel.flick.ui.theme.Gray70
import com.ikuzMirel.flick.ui.theme.museoRegular

@Composable
fun UserInputTextField(
    textFieldValue: String,
    onTextFieldValueChange: (String) -> Unit,
    maxLines: Int = 1,
    hintText: String,
    keyboardOptions: KeyboardOptions = KeyboardOptions(
        keyboardType = KeyboardType.Text,
        imeAction = ImeAction.Search
    ),
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    actionIcon: ImageVector,
    onActionClick: () -> Unit
) {
    Surface(
        shadowElevation = 4.dp,
        shape = RoundedCornerShape(15.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Gray70,
//                    when {
//                        isSystemInDarkTheme() -> Gray70
//                        else -> Gray30
//                    },
                    RoundedCornerShape(15.dp)
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            BasicTextField(
                value = textFieldValue,
                onValueChange = { onTextFieldValueChange(it) },
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 16.dp, end = 8.dp),
                maxLines = maxLines,
                keyboardOptions = keyboardOptions,
                keyboardActions = keyboardActions,
                cursorBrush = SolidColor(MaterialTheme.colorScheme.onBackground),
                textStyle = TextStyle(
                    color = MaterialTheme.colorScheme.onBackground,
                    fontFamily = museoRegular,
                    fontSize = 16.sp
                ),
                decorationBox = { innerTextField ->
                    Box {
                        if (textFieldValue.isEmpty()) {
                            Text(
                                text = hintText,
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
                    .background(MaterialTheme.colorScheme.background, RoundedCornerShape(12.dp))
                    .clickable {
                        onActionClick()
                    },
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = actionIcon,
                    contentDescription = "",
                    modifier = Modifier
                        .size(24.dp)
                        .align(Alignment.CenterHorizontally)
                )
            }
        }
    }
}
