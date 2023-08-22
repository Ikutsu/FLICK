package com.ikuzMirel.flick.ui.home.map

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Construction
import androidx.compose.runtime.Composable
import com.ikuzMirel.flick.ui.components.emptyState.SimpleEmptyStateScreen

@Composable
fun Map() {
//    GoogleMap(
//        modifier = Modifier
//            .fillMaxSize(),
//        uiSettings = MapUiSettings(
//            mapToolbarEnabled = false,
//            zoomControlsEnabled = false
//        ),
//        properties = MapProperties(
//            mapStyleOptions = MapStyleOptions(
//                when{
//                    isSystemInDarkTheme() -> MapStyle.nightMap
//                    else -> MapStyle.dayMap
//                }
//            )
//        ),
//    )
    SimpleEmptyStateScreen(
        icon = Icons.Filled.Construction,
        title = "Work In Progress",
        buttonShow = false
    )
}

