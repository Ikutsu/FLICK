package com.ikuzMirel.flick.ui.home.contact

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ikuzMirel.flick.data.room.dao.FriendDao
import com.ikuzMirel.flick.domain.entities.toFriend
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ContactViewModel @Inject constructor(
    private val friendDao: FriendDao
) : ViewModel() {

    private val _uiState: MutableStateFlow<ContactUIState> = MutableStateFlow(ContactUIState())
    val uiState: StateFlow<ContactUIState> = _uiState.asStateFlow()

    fun getFriends() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(isLoading = true)
            }
            friendDao.getAllFriends().collect { friends ->
                _uiState.update { state ->
                    state.copy(
                        isLoading = false,
                        friends = friends.map {
                            it.toFriend()
                        }
                    )
                }
            }
        }
    }
}