package com.ikuzMirel.flick.ui.mainContent.contact

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ikuzMirel.flick.data.dto.friendList.request.FriendListRequestDto
import com.ikuzMirel.flick.data.repositories.PreferencesRepository
import com.ikuzMirel.flick.data.repositories.UserRepository
import com.ikuzMirel.flick.data.utils.ResponseResult
import com.ikuzMirel.flick.domain.model.Friend
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ContactViewModel @Inject constructor(
    private val preferencesRepository: PreferencesRepository,
    private val userRepository: UserRepository
): ViewModel(){

    private val _userId: MutableStateFlow<String> = MutableStateFlow("")
    val userId: StateFlow<String> = _userId.asStateFlow()

    val state = mutableStateOf(ContactUIState())

    init {
        viewModelScope.launch {
            state.value = state.value.copy(isLoading = true)
            _userId.value = preferencesRepository.getUserId().data!!
        }
    }

    fun getFriends() {
        viewModelScope.launch {
            state.value = state.value.copy(isLoading = true)
            val request = FriendListRequestDto(
                id = userId.value,
                token = preferencesRepository.getJwt().data!!
            )
            userRepository.gerUserFriends(request).collect { response ->
                when (response) {
                    is ResponseResult.Error -> {
                        state.value = state.value.copy(isLoading = false)
                    }
                    is ResponseResult.Success -> {
                        if (response.data == null) {
                            state.value = state.value.copy(isLoading = false)
                        } else {
                            state.value = state.value.copy(
                                isLoading = false,
                                friends = response.data.data!!.friends.map {
                                    Friend (
                                        status = 1,
                                        name = it.username,
                                        userId = it.id,
                                        collectionId = it.collectionId,
                                        latestMessage = "Hello",
                                        notification = 1,
                                    )
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}