package com.ikuzMirel.flick.ui.setting

import android.content.Intent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ikuzMirel.flick.data.remote.websocket.WebSocketService
import com.ikuzMirel.flick.data.service.NetworkService
import com.ikuzMirel.flick.ui.destinations.AuthenticationDestination
import com.ikuzMirel.flick.ui.destinations.MainContentDestination
import com.ikuzMirel.flick.ui.destinations.SplashDestination
import com.ikuzMirel.flick.ui.destinations.WelcomeDestination
import com.ikuzMirel.flick.ui.theme.Red50
import com.ikuzMirel.flick.ui.theme.cocogooseBold
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.runBlocking

@Destination
@Composable
fun Setting(
    navigator: DestinationsNavigator,
    viewModel: SettingViewModel = hiltViewModel()
) {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Button(
            onClick = {
                viewModel.logout()
                context.stopService(Intent(context, NetworkService::class.java))
                runBlocking {
                    navigator.navigate(AuthenticationDestination(true)){
                        popUpTo(MainContentDestination.route){
                            inclusive = true
                        }
                    }
                }
            },
            modifier = Modifier
                .width(300.dp)
                .height(52.dp),
            shape = RoundedCornerShape(15.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Red50
            )
        ) {
            Text(
                text = "Log out",
                fontSize = 18.sp,
                color = Color.White,
                fontFamily = cocogooseBold
            )
        }
    }
}
