package com.ikuzMirel.flick

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.ikuzMirel.flick.ui.NavGraphs
import com.ikuzMirel.flick.ui.chat.Chat
import com.ikuzMirel.flick.ui.mainContent.MainContent
import com.ikuzMirel.flick.ui.theme.FLICKTheme
import com.ikuzMirel.flick.ui.theme.Gray80
import com.ikuzMirel.flick.ui.welcome.Home
import com.ramcosta.composedestinations.DestinationsNavHost

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
                DestinationsNavHost(navGraph = NavGraphs.root)
            }
        }
    }
}