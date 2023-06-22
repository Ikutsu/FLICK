package com.ikuzMirel.flick.ui.home.map

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings

@Composable
fun Map() {
    GoogleMap(
        modifier = Modifier
            .fillMaxSize(),
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

