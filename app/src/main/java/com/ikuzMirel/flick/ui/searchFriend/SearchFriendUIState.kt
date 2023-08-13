package com.ikuzMirel.flick.ui.searchFriend

import com.ikuzMirel.flick.domain.model.UserSearchResult

data class SearchFriendUIState(
    val searchQuery: String = "",
    val searchResults: List<UserSearchResult> = emptyList(),
    val showEmptyState : Boolean = false,
)
