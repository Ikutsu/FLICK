package com.ikuzMirel.flick.ui.components.textFields

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.focusTarget
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.ikuzMirel.flick.ui.theme.Gray30
import com.ikuzMirel.flick.ui.theme.Gray70
import com.ikuzMirel.flick.ui.theme.cocogooseLight

@Composable
fun IconHintTextField(
    leadingIcon: ImageVector,
    gotFocusIcon: ImageVector,
    placeholder: String,
    imeAction: ImeAction,
    value: TextFieldValue,
    onValueChange: (value : TextFieldValue) -> Unit,
    focusRequester: FocusRequester,
    keyboardActions: KeyboardActions,
    visualTransformation: VisualTransformation,
    textStyle: TextStyle = TextStyle(
        color = MaterialTheme.colorScheme.onBackground,
        fontFamily = cocogooseLight
    )
) {
    var icon by remember { mutableStateOf(leadingIcon) }
    var hint by remember { mutableStateOf(placeholder) }
    Column (
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 32.dp)
    ){
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp)
                .background(
                    when {
                        isSystemInDarkTheme() -> Gray70
                        else -> Gray30
                    },
                    RoundedCornerShape(15.dp)
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = "",
                modifier = Modifier
                    .padding(start = 14.dp, end = 8.dp)
                    .size(24.dp)
                    .focusTarget(),
                tint = MaterialTheme.colorScheme.onSurface
            )
            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 8.dp)
                    .onFocusChanged {
                        icon = if (it.isFocused) gotFocusIcon else leadingIcon
                        hint = if (it.isFocused) "" else placeholder
                    }
                    .focusRequester(focusRequester),
                singleLine = true,
                maxLines = 1,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = imeAction
                ),
                keyboardActions = keyboardActions,
                cursorBrush = SolidColor(MaterialTheme.colorScheme.onBackground),
                textStyle = textStyle,
                visualTransformation = visualTransformation,
                decorationBox = { innerTextField ->
                    Box {
                        if (value.text.isEmpty()) {
                            Text(
                                text = hint,
                                color = MaterialTheme.colorScheme.onBackground,
                                modifier = Modifier
                                    .alpha(0.5f),
                                fontFamily = cocogooseLight //Roboto regular
                            )
                        }
                        innerTextField()
                    }
                }
            )
        }
    }
}