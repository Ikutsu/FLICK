package com.ikuzMirel.flick.ui.splash

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ikuzMirel.flick.R
import com.ikuzMirel.flick.data.auth.AuthResult
import com.ikuzMirel.flick.ui.authentication.AuthViewModel
import com.ikuzMirel.flick.ui.destinations.MainContentDestination
import com.ikuzMirel.flick.ui.destinations.SplashDestination
import com.ikuzMirel.flick.ui.destinations.WelcomeDestination
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@RootNavGraph(start = true)
@Destination
@Composable
fun Splash(
    navigator: DestinationsNavigator,
    viewModel: AuthViewModel = hiltViewModel()
) {
    val state = viewModel.state
    val context = LocalContext.current
    val hasToken = state.hasToken

    LaunchedEffect(true) {
        viewModel.authResult.collect { result ->
            when (result) {
                is AuthResult.Authenticated -> {
                    state.keepOnScreenCondition = false
                    navigator.popBackStack(SplashDestination.route, inclusive = true)
                    navigator.navigate(MainContentDestination) {
                        popUpTo(SplashDestination.route) {
                            inclusive = true
                        }
                    }
                }
                is AuthResult.Unauthenticated -> {
                    navigator.popBackStack(SplashDestination.route, inclusive = true)
                    navigator.navigate(WelcomeDestination)
                    state.keepOnScreenCondition = false
                }
                is AuthResult.NoConnectionError -> {
                    navigator.navigate(WelcomeDestination) //TODO: NO INTERNET SCREEN
                    navigator.clearBackStack(SplashDestination)
                    state.keepOnScreenCondition = false
                    Toast.makeText(
                        context,
                        "No internet connection",
                        Toast.LENGTH_LONG
                    ).show()
                }
                is AuthResult.SocketTimeoutException -> {
                    navigator.navigate(WelcomeDestination) //TODO: NO INTERNET SCREEN
                    navigator.clearBackStack(SplashDestination)
                    state.keepOnScreenCondition = false
                    Toast.makeText(
                        context,
                        "Connection timed out",
                        Toast.LENGTH_LONG
                    ).show()
                }
                else -> {
                }
            }
        }
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .offset(y = (-12).dp),
        contentAlignment = Alignment.Center
    ) {
        Image(
            modifier = Modifier
                .size(290.dp),
            painter = painterResource(id = R.drawable.flick_splash),
            contentDescription = ""
        )
        CircularProgressIndicator(
            modifier = Modifier
                .size(36.dp)
                .offset(y = 112.dp),
            strokeWidth = 4.dp,
            color = Color.White,
            strokeCap = StrokeCap.Round
        )
    }
}