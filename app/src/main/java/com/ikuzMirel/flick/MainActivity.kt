package com.ikuzMirel.flick

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.ikuzMirel.flick.ui.login.Login
import com.ikuzMirel.flick.ui.map.MapScreen
import com.ikuzMirel.flick.ui.sighUp.SignUp
import com.ikuzMirel.flick.ui.story.Story
import com.ikuzMirel.flick.ui.theme.FLICKTheme
import com.ikuzMirel.flick.ui.theme.Gray80
import com.ikuzMirel.flick.ui.theme.Purple10
import com.ikuzMirel.flick.ui.welcome.Home

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FLICKTheme {
                val systemUiController = rememberSystemUiController()
                val useDarkIcons = !isSystemInDarkTheme()
                val darkTheme = when{
                    isSystemInDarkTheme() -> Gray80
                    else -> Color.White
                }
                SideEffect {
                    systemUiController.setSystemBarsColor(darkTheme, darkIcons = useDarkIcons)
                }
                Story()
            }
        }
    }
}