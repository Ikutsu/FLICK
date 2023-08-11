package com.ikuzMirel.flick.ui.welcome

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ikuzMirel.flick.R
import com.ikuzMirel.flick.data.response.BasicResponse
import com.ikuzMirel.flick.ui.authentication.AuthViewModel
import com.ikuzMirel.flick.ui.destinations.AuthenticationDestination
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator


@Destination
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun Welcome(
    navigator: DestinationsNavigator,
    viewModel: AuthViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(state, context) {
        viewModel.authResult.collect { result ->
            when (result) {
//                is AuthResult.Authenticated -> {
//                    navigator.navigate(MainContentDestination()) {
//                        popUpTo(AuthenticationDestination.route) {
//                            inclusive = true
//                        }
//                    }
//                }
//                is AuthResult.Unauthenticated -> {
//                    if (hasToken) {
//                        Toast.makeText(
//                            context,
//                            "Your session has expired, please log in again",
//                            Toast.LENGTH_LONG
//                        ).show()
//                        navigator.navigate(AuthenticationDestination()) {
//                            popUpTo(MainContentDestination.route) {
//                                inclusive = true
//                            }
//                        }
//                    }
//                }
                is BasicResponse.Error -> {
                    Toast.makeText(
                        context,
                        "Error",
                        Toast.LENGTH_LONG
                    ).show()
                }
                else -> {
                }
            }
        }
    }

    Scaffold() {
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(
                    id = R.drawable.flick_welcome_light
//                    when {
//                        isSystemInDarkTheme() -> R.drawable.flick_4x
//                        else -> R.drawable.flick_dark
//                    }
                ),
                contentDescription = "",
                alpha = 0.3f,
                contentScale = ContentScale.FillHeight,
            )
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(96.dp)
                )
                Text(
                    text = "FLICK",
                    fontSize = 128.sp,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontWeight = FontWeight.Bold
                )
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                )
                Button(
                    onClick = {
                        navigator.popBackStack()
                        navigator.navigate(AuthenticationDestination())
                    },
                    modifier = Modifier
                        .width(300.dp)
                        .height(52.dp),
                    shape = RoundedCornerShape(15.dp)
                ) {
                    Text(
                        text = "Get Started",
                        fontSize = 18.sp,
                    )
                }
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(46.dp)
                )
            }
        }

    }
}