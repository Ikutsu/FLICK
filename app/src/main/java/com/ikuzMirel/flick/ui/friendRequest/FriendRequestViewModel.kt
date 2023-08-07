package com.ikuzMirel.flick.ui.friendRequest

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ikuzMirel.flick.data.repositories.FriendReqRepository
import com.ikuzMirel.flick.data.repositories.PreferencesRepository
import com.ikuzMirel.flick.data.repositories.UserRepository
import com.ikuzMirel.flick.data.requests.FriendListRequest
import com.ikuzMirel.flick.data.requests.FriendReqRequest
import com.ikuzMirel.flick.data.response.BasicResponse
import com.ikuzMirel.flick.data.room.dao.FriendDao
import com.ikuzMirel.flick.data.room.dao.FriendReqDao
import com.ikuzMirel.flick.domain.entities.FriendRequestStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FriendRequestViewModel @Inject constructor(
    private val preferencesRepository: PreferencesRepository,
    private val friendReqRepository: FriendReqRepository,
    private val friendReqDao: FriendReqDao,
    private val userRepository: UserRepository,
    private val friendDao: FriendDao
) : ViewModel() {

    private val _uiState: MutableStateFlow<FriendRequestUIState> =
        MutableStateFlow(FriendRequestUIState())
    val uiState: StateFlow<FriendRequestUIState> = _uiState.asStateFlow()

    private val resultChannel = Channel<BasicResponse<String>>()
    val requestResult = resultChannel.receiveAsFlow()

    init {
        viewModelScope.launch {
            val userId = preferencesRepository.getUserId()
            fetchFriendRequests(userId)
        }
    }

    private suspend fun fetchFriendRequests(userId: String) {
        friendReqDao.getAllFriendReqs().collect { result ->
            result.forEach {
                if (it.receiverId == userId) {
                    if (it.status == FriendRequestStatus.PENDING.name) {
                        _uiState.update { uiState ->
                            uiState.copy(
                                receivedRequests = uiState.receivedRequests + it
                            )
                        }
                    }
                } else {
                    _uiState.update { uiState ->
                        uiState.copy(
                            sentRequests = uiState.sentRequests + it
                        )
                    }
                }
            }
        }
    }

    fun onAcceptRequest(friendReqId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val request = FriendReqRequest(friendReqId)
            friendReqRepository.acceptFriendRequest(request).collect { response ->
                when (response) {
                    is BasicResponse.Success -> {
                        val friendRequest = friendReqDao.getFriendReq(request.requestId).copy(
                            status = FriendRequestStatus.ACCEPTED.name
                        )
                        friendReqDao.upsertFriendReq(friendRequest)

                        val newFriendListRequest = FriendListRequest(
                            preferencesRepository.getUserId(),
                            preferencesRepository.getJwt()
                        )
                        val newFriendList =
                            userRepository.getUserFriends(newFriendListRequest).first()
                        _uiState.update { state ->
                            val list = state.receivedRequests.toMutableList()
                            val data = list.find { it.id == friendReqId }
                            val index = state.receivedRequests.indexOf(data)
                            list[index] = data!!.copy( status = FriendRequestStatus.ACCEPTED.name )

                            state.copy(
                                receivedRequests = list.toList()
                            )
                        }

                        if (newFriendList is BasicResponse.Success) {
                            newFriendList.data?.friends?.forEach {
                                friendDao.upsertFriend(it)
                            }
                        }
                        resultChannel.send(BasicResponse.Success("Friend request accepted"))
                    }

                    is BasicResponse.Error -> {
                        resultChannel.send(BasicResponse.Error(response.errorMessage))
                    }
                }
            }
        }
    }

    fun onRejectRequest(friendReqId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val request = FriendReqRequest(friendReqId)
            friendReqRepository.rejectFriendRequest(request).collect {
                when (it) {
                    is BasicResponse.Success -> {
                        val friendRequest = friendReqDao.getFriendReq(request.requestId).copy(
                            status = FriendRequestStatus.REJECTED.name
                        )
                        friendReqDao.upsertFriendReq(friendRequest)

                        _uiState.update { state ->
                            val list = state.receivedRequests.toMutableList()
                            val data = list.find { it.id == friendReqId }
                            val index = state.receivedRequests.indexOf(data)
                            list[index] = data!!.copy(
                                status = FriendRequestStatus.REJECTED.name
                            )

                            state.copy(
                                receivedRequests = list.toList()
                            )
                        }
                        resultChannel.send(BasicResponse.Success("Friend request rejected"))
                    }

                    is BasicResponse.Error -> {
                        resultChannel.send(BasicResponse.Error(it.errorMessage))
                    }
                }
            }
        }
    }

    fun onCancelRequest(friendReqId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val request = FriendReqRequest(friendReqId)
            friendReqRepository.cancelFriendRequest(request).collect {
                when (it) {
                    is BasicResponse.Success -> {
                        friendReqDao.deleteFriendReq(friendReqId)

                        _uiState.update { state ->
                            val list = state.sentRequests.toMutableList()
                            val data = list.find { it.id == friendReqId }
                            val index = state.sentRequests.indexOf(data)
                            list[index] = data!!.copy(
                                status = FriendRequestStatus.CANCELLED.name
                            )

                            state.copy(
                                sentRequests = list.toList()
                            )
                        }
                        resultChannel.send(BasicResponse.Success("Friend request cancelled"))
                    }

                    is BasicResponse.Error -> {
                        resultChannel.send(BasicResponse.Error(it.errorMessage))
                    }
                }
            }
        }
    }

    fun getCollectionId(userId: String): String {
        var cid = ""
        viewModelScope.launch {
            cid = friendDao.getFriend(userId).first().collectionId
        }
        return cid
    }
}