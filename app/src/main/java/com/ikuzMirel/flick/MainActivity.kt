package com.ikuzMirel.flick

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.ikuzMirel.flick.ui.theme.FLICKTheme
import com.ikuzMirel.flick.ui.theme.Gray80
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
                Home()
            }
        }
    }
}