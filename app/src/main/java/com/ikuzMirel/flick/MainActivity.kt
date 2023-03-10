package com.ikuzMirel.flick

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.ikuzMirel.flick.data.auth.AuthResult
import com.ikuzMirel.flick.ui.NavGraphs
import com.ikuzMirel.flick.ui.authentication.AuthViewModel
import com.ikuzMirel.flick.ui.destinations.MainContentDestination
import com.ikuzMirel.flick.ui.destinations.WelcomeDestination
import com.ikuzMirel.flick.ui.theme.FLICKTheme
import com.ikuzMirel.flick.ui.theme.Gray80
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.spec.Route
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel by viewModels<AuthViewModel>()
    private var keepOnScreenCondition = true
    private val isInit by lazy {
        viewModel.state.isInit
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        splashScreen.setKeepOnScreenCondition { keepOnScreenCondition }
        setContent {
            FLICKTheme {
                val systemUiController = rememberSystemUiController()
                val useDarkIcons = !isSystemInDarkTheme()
                val darkTheme = when {
                    isSystemInDarkTheme() -> Gray80
                    else -> Color.White
                }
                LaunchedEffect(true){
                    viewModel.authResult.collect { result ->
                        when (result) {
                            is AuthResult.Error -> {
                            }
                            else -> {
                                keepOnScreenCondition = false
                            }
                        }
                    }
                }
                SideEffect {
                    systemUiController.setSystemBarsColor(darkTheme, darkIcons = useDarkIcons)
                }
                DestinationsNavHost(
                    navGraph = NavGraphs.root
                )
            }
        }
    }
}