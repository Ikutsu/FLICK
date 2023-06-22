package com.ikuzMirel.flick.ui.home.contact

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ikuzMirel.flick.data.room.dao.FriendDao
import com.ikuzMirel.flick.domain.entities.toFriend
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ContactViewModel @Inject constructor(
    private val friendDao: FriendDao
) : ViewModel() {

    val state = mutableStateOf(ContactUIState())
    fun getFriends() {
        viewModelScope.launch {
            state.value = state.value.copy(isLoading = true)
            friendDao.getAllFriends().collect { friends ->
                state.value = state.value.copy(
                    isLoading = false,
                    friends = friends.map {
                        it.toFriend()
                    }
                )
            }
        }
    }
}