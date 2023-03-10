package com.ikuzMirel.flick.ui.mainContent.contact

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ikuzMirel.flick.data.user.UserPreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ContactViewModel @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository
): ViewModel(){

    private val _userId: MutableStateFlow<String> = MutableStateFlow("")
    val userId: StateFlow<String> = _userId.asStateFlow()

    init {
        viewModelScope.launch {
            _userId.value = userPreferencesRepository.getUserId()
        }
    }

}