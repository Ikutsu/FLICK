package com.ikuzMirel.flick

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.ikuzMirel.flick.data.response.BasicResponse
import com.ikuzMirel.flick.ui.NavGraphs
import com.ikuzMirel.flick.ui.authentication.AuthViewModel
import com.ikuzMirel.flick.ui.destinations.HomeDestination
import com.ikuzMirel.flick.ui.destinations.WelcomeDestination
import com.ikuzMirel.flick.ui.theme.FLICKTheme
import com.ikuzMirel.flick.ui.theme.Gray80
import com.ramcosta.composedestinations.DestinationsNavHost
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
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

                val darkTheme = Gray80
//                    when {
//                    isSystemInDarkTheme() -> Gray80
//                    else -> Color.White
//                }

                var isAuthenticated by remember { mutableStateOf(false) }

                LaunchedEffect(Unit) {
                    withContext(Dispatchers.IO) {
                        viewModel.authenticate()
                        viewModel.checkToken()
                        viewModel.authResult.collect {
                            isAuthenticated = when (it) {
                                is BasicResponse.Success -> true
                                is BasicResponse.Error -> false
                            }
                            keepOnScreenCondition = false
                        }
                    }
                }
                SideEffect {
                    systemUiController.setSystemBarsColor(darkTheme, darkIcons = false)
                }
                if (isAuthenticated) {
                    DestinationsNavHost(
                        navGraph = NavGraphs.root,
                        navController = navController,
                        startRoute = HomeDestination
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
}