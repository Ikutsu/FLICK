package com.ikuzMirel.flick.ui.friendRequest

import com.ikuzMirel.flick.domain.entities.FriendRequestEntity

data class FriendRequestUIState(
    val receivedRequests: List<FriendRequestEntity> = emptyList(),
    val sentRequests: List<FriendRequestEntity> = emptyList(),
    val isLoading: Boolean = false
)