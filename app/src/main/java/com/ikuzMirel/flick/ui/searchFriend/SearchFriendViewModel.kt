package com.ikuzMirel.flick.ui.searchFriend

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ikuzMirel.flick.data.repositories.FriendReqRepository
import com.ikuzMirel.flick.data.repositories.PreferencesRepository
import com.ikuzMirel.flick.data.repositories.UserRepository
import com.ikuzMirel.flick.data.requests.SendFriendReqRequest
import com.ikuzMirel.flick.data.response.BasicResponse
import com.ikuzMirel.flick.data.room.dao.FriendReqDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchFriendViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val preferencesRepository: PreferencesRepository,
    private val friendReqRepository: FriendReqRepository,
    private val friendReqDao: FriendReqDao
) : ViewModel() {

    private val _uiState: MutableStateFlow<SearchFriendUIState> =
        MutableStateFlow(SearchFriendUIState())
    val uiState: StateFlow<SearchFriendUIState> = _uiState.asStateFlow()

    private val resultChannel = Channel<BasicResponse<String>>()
    val actionResult = resultChannel.receiveAsFlow()

    private var token = mutableStateOf("")
    private var userId = mutableStateOf("")

    init {
        viewModelScope.launch {
            token.value = preferencesRepository.getValue(PreferencesRepository.TOKEN)!!
            userId.value = preferencesRepository.getValue(PreferencesRepository.USERID)!!
        }
    }

    fun onSearchQueryChanged(query: String) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(searchQuery = query)
            }
            if (query.isEmpty()) {
                _uiState.update {
                    it.copy(searchResults = emptyList())
                }
                return@launch
            }
            userRepository.searchUsers(query).collect { response ->
                when (response) {
                    is BasicResponse.Success -> {
                        _uiState.update {
                            it.copy(
                                searchResults = response.data?.users!!,
                                showEmptyState = response.data.users.isEmpty()
                            )
                        }
                    }

                    is BasicResponse.Error -> {
                        _uiState.update {
                            it.copy(searchResults = emptyList())
                        }
                    }
                }
            }
        }
    }

    fun onAddFriendClicked(receiverId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val request = SendFriendReqRequest(
                senderId = userId.value,
                receiverId = receiverId
            )
            friendReqRepository.sendFriendRequest(request).collect {
                when (it) {
                    is BasicResponse.Success -> {
                        friendReqDao.upsertFriendReq(it.data!!)
                        resultChannel.send(BasicResponse.Success("Friend request sent"))
                    }
                    is BasicResponse.Error -> {
                        resultChannel.send(BasicResponse.Error(it.errorMessage))
                    }
                }
            }
        }
    }
}