package com.ikuzMirel.flick.ui.welcome

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ikuzMirel.flick.R
import com.ikuzMirel.flick.ui.theme.FLICKTheme
import com.ikuzMirel.flick.ui.theme.robotoCondensed

@Preview (showBackground = true)
@Composable
fun Preview(){
    FLICKTheme(
        darkTheme = true
    ) {
        Home()
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Home(){
    Scaffold() {
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.flick_4x),
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
                    fontFamily = robotoCondensed
                )
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                )
                Button(
                    onClick = { /*TODO*/ },
                    modifier = Modifier
                        .width(300.dp)
                        .height(52.dp),
                    shape = RoundedCornerShape(15.dp)
                ) {
                    Text(
                        text = "Get Started",
                        fontSize = 18.sp
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