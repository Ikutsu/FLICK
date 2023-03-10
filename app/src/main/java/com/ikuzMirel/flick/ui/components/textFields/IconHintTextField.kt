package com.ikuzMirel.flick.ui.components.textFields

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButton
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.*
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ikuzMirel.flick.ui.theme.*

@Composable
fun IconHintTextField(
    leadingIcon: ImageVector,
    gotFocusIcon: ImageVector,
    placeholder: String,
    imeAction: ImeAction,
    value: String,
    onValueChange: (value: String) -> Unit,
    focusRequester: FocusRequester,
    keyboardActions: KeyboardActions,
    error: Boolean = false,
    errorMsg: String = "",
    isPassword: Boolean = false,
    height: Dp = 78.dp,
    alpha: Float = 1f,
) {
    var icon by remember { mutableStateOf(leadingIcon) }
    var hint by remember { mutableStateOf(placeholder) }
    var passwordVisibility by remember { mutableStateOf(false) }
    val errorAlpha by animateFloatAsState(
        targetValue = if (error) 1f else 0f,
        animationSpec = tween(200)
    )
    val fieldPadding = when (isPassword) {
        true -> 0.dp
        false -> 8.dp
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(height)
            .alpha(alpha)
    ) {
        Row(
            modifier = when (error) {
                true -> Modifier
                    .fillMaxWidth()
                    .height(54.dp)
                    .padding(horizontal = 32.dp)
                    .border(1.dp, Red50, RoundedCornerShape(15.dp))
                    .background(
                        when {
                            isSystemInDarkTheme() -> Gray70
                            else -> Gray30
                        },
                        RoundedCornerShape(15.dp)
                    )
                false -> Modifier
                    .fillMaxWidth()
                    .height(54.dp)
                    .padding(horizontal = 32.dp)
                    .background(
                        when {
                            isSystemInDarkTheme() -> Gray70
                            else -> Gray30
                        },
                        RoundedCornerShape(15.dp)
                    )
            },
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
                    .weight(1f)
                    .padding(end = fieldPadding)
                    .onFocusChanged {
                        icon = if (it.isFocused) gotFocusIcon else leadingIcon
                        hint = if (it.isFocused) "" else placeholder
                    }
                    .focusRequester(focusRequester)
                    .height(18.dp),
                singleLine = true,
                maxLines = 1,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = imeAction
                ),
                keyboardActions = keyboardActions,
                cursorBrush = SolidColor(MaterialTheme.colorScheme.onBackground),
                textStyle = when (isPassword) {
                    true -> when (passwordVisibility) {
                        true -> TextStyle(
                            color = MaterialTheme.colorScheme.onBackground,
                            fontFamily = museoRegular,
                            fontSize = 16.sp
                        )
                        false -> TextStyle(
                            color = MaterialTheme.colorScheme.onBackground,
                            fontFamily = museoRegular,
                            fontSize = 16.sp,
                            letterSpacing = 1.sp
                        )
                    }
                    false -> TextStyle(
                        color = MaterialTheme.colorScheme.onBackground,
                        fontFamily = museoRegular,
                        fontSize = 16.sp
                    )
                },
                visualTransformation = when (isPassword) {
                    true -> when (passwordVisibility) {
                        true -> VisualTransformation.None
                        false -> PasswordVisualTransformation()
                    }
                    false -> VisualTransformation.None
                },
                decorationBox = { innerTextField ->
                    Box () {
                        if (value.isEmpty()) {
                            Text(
                                text = hint,
                                color = MaterialTheme.colorScheme.onBackground,
                                modifier = Modifier
                                    .alpha(0.5f)
                                    .offset(y = (-2).dp),
                                fontFamily = cocogooseLight
                            )
                        }
                        innerTextField()
                    }
                }
            )
            if (isPassword) {
                IconToggleButton(
                    checked = false,
                    onCheckedChange = { passwordVisibility = !passwordVisibility }) {
                    Icon(
                        imageVector = when (passwordVisibility) {
                            true -> Icons.Outlined.VisibilityOff
                            false -> Icons.Filled.Visibility
                        },
                        contentDescription = "Show password"
                    )
                }
            }
        }
        Text(
            text = errorMsg,
            color = Red50,
            modifier = Modifier
                .padding(top = 2.dp, start = 46.dp)
                .alpha(errorAlpha),
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
        )
    }
}