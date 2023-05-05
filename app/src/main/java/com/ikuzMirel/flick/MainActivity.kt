package com.ikuzMirel.flick

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.ikuzMirel.flick.data.remote.websocket.WebSocketService
import com.ikuzMirel.flick.data.service.NetworkService
import com.ikuzMirel.flick.data.utils.ResponseResult
import com.ikuzMirel.flick.ui.NavGraphs
import com.ikuzMirel.flick.ui.authentication.AuthViewModel
import com.ikuzMirel.flick.ui.destinations.MainContentDestination
import com.ikuzMirel.flick.ui.destinations.WelcomeDestination
import com.ikuzMirel.flick.ui.theme.FLICKTheme
import com.ikuzMirel.flick.ui.theme.Gray80
import com.ramcosta.composedestinations.DestinationsNavHost
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: AuthViewModel by viewModels()
    private var keepOnScreenCondition = true

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        splashScreen.setKeepOnScreenCondition { keepOnScreenCondition }
        setContent {
            FLICKTheme {
                val systemUiController = rememberSystemUiController()
                val useDarkIcons = !isSystemInDarkTheme()
                val navController = rememberNavController()

                val darkTheme = when {
                    isSystemInDarkTheme() -> Gray80
                    else -> Color.White
                }

                var isAuthenticated by remember { mutableStateOf(false) }

                LaunchedEffect(Unit) {
                    withContext(Dispatchers.IO) {
                        viewModel.authenticate()
                        viewModel.checkToken()
                        viewModel.authResult.collect {
                            println("$it main")
                            isAuthenticated = when (it) {
                                is ResponseResult.Success -> true
                                is ResponseResult.Error -> false
                            }
                            keepOnScreenCondition = false
                        }
                    }
                }
                SideEffect {
                    systemUiController.setSystemBarsColor(darkTheme, darkIcons = useDarkIcons)
                }
                if (isAuthenticated) {
                    DestinationsNavHost(
                        navGraph = NavGraphs.root,
                        navController = navController,
                        startRoute = MainContentDestination
                    )
                } else {
                    DestinationsNavHost(
                        navGraph = NavGraphs.root,
                        navController = navController,
                        startRoute = WelcomeDestination
                    )
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        stopService(Intent(this, NetworkService::class.java))
    }
}