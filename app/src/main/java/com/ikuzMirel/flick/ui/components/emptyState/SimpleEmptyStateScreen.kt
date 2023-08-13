package com.ikuzMirel.flick.ui.components.emptyState

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.ikuzMirel.flick.ui.theme.Gray50

@Composable
fun SimpleEmptyStateScreen(
    icon: ImageVector,
    title: String,
    description: String = "",
    buttonShow: Boolean = true,
    buttonText: String = "",
    titleModifier: Modifier = Modifier,
    descriptionModifier: Modifier = Modifier,
    onButtonClick: () -> Unit = {}
){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = "",
            tint = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier
                .size(96.dp)
        )
        Text(
            text = title,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = titleModifier
        )
        if (description.isNotEmpty()) {
            Text(
                text = description,
                style = MaterialTheme.typography.bodyLarge,
                color = Gray50,
                modifier = descriptionModifier,
                textAlign = TextAlign.Center,

            )
        }
        if (buttonShow){
            Button(
                onClick = { onButtonClick() },
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .padding(top = 8.dp)
            ) {
                Text(
                    text = buttonText,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.SemiBold,
                )
            }
        }
    }
}