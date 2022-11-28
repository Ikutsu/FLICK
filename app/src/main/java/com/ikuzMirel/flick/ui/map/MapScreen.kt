package com.ikuzMirel.flick.ui.map

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.ikuzMirel.flick.ui.components.topAppBars.NavOnlyTopBar

@Preview
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapScreen() {
    Scaffold(
        topBar = { NavOnlyTopBar {}}, //TODO: Change when chat screen is done
        bottomBar = { NavigationBar() {} }, //TODO: Change when chat screen is done
        modifier = Modifier
            .fillMaxSize()
    ) { paddingValues ->
        GoogleMap(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            uiSettings = MapUiSettings(
                mapToolbarEnabled = false,
                zoomControlsEnabled = false
            ),
            properties = MapProperties(
                mapStyleOptions = MapStyleOptions(
                    when{
                        isSystemInDarkTheme() -> MapStyle.nightMap
                        else -> MapStyle.dayMap
                    }
                )
            ),

        )
    }
}

