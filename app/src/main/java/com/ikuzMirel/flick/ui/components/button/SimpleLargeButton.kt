package com.ikuzMirel.flick.ui.components.button

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@Composable
fun SimpleLargeButton(
    onClick: () -> Unit,
    icon: ImageVector,
    tint: Color = Color.Black
) {
    Column(
        modifier = Modifier
            .padding(end = 8.dp)
            .size(48.dp)
            .background(
                Color.White,
                RoundedCornerShape(12.dp)
            )
            .clickable {
                onClick()
            },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            tint = tint,
            imageVector = icon,
            contentDescription = "",
            modifier = Modifier
                .size(36.dp)
                .align(Alignment.CenterHorizontally)
        )
    }
}