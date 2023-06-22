package com.ikuzMirel.flick.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.OutOfQuotaPolicy
import androidx.work.WorkerParameters
import com.ikuzMirel.flick.data.repositories.ChatRepository
import com.ikuzMirel.flick.data.repositories.PreferencesRepository
import com.ikuzMirel.flick.data.repositories.UserRepository
import com.ikuzMirel.flick.data.requests.FriendListRequest
import com.ikuzMirel.flick.data.requests.MessageListRequest
import com.ikuzMirel.flick.data.response.BasicResponse
import com.ikuzMirel.flick.data.room.dao.FriendDao
import com.ikuzMirel.flick.data.room.dao.MessageDao
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext

@HiltWorker
class SyncWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val preferencesRepository: PreferencesRepository,
    private val chatRepository: ChatRepository,
    private val userRepository: UserRepository,
    private val friendDao: FriendDao,
    private val messageDao: MessageDao
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        val jwt = preferencesRepository.getJwt()
        val userId = preferencesRepository.getUserId()

        if (jwt.isBlank() || userId.isBlank()) {
           Result.failure()
        }

        val syncSuccessOne = fetchFriendListAndUpdateDB(jwt, userId)
        val syncSuccessTwo = fetchMessagesAndUpdateDB(jwt)


        if (syncSuccessOne && syncSuccessTwo) {
            Result.success()
        } else {
            Result.retry()
        }
    }


    private suspend fun fetchFriendListAndUpdateDB(jwt: String, userId: String): Boolean {
        var success = false
        val response = userRepository.gerUserFriends(FriendListRequest(userId, jwt)).first()
        if (response is BasicResponse.Success) {
            response.data?.friends?.forEach {
                friendDao.upsertFriend(it)
            }
            success = true
        }
        return success
    }

    private suspend fun fetchMessagesAndUpdateDB(jwt: String): Boolean {
        var success = false
        val cids = friendDao.getAllFriendsCIDs().first()
        if (cids.isEmpty()) {
            success = true
        } else {
            for (id in cids) {
                val result = chatRepository.getChatMassages(MessageListRequest(id, jwt)).first()
                if (result is BasicResponse.Success) {
                    result.data?.messages?.forEach {
                        messageDao.upsertMessage(it)
                    }
                    success = true
                }
            }
        }
        return success
    }

    companion object {
        fun startWork() = OneTimeWorkRequestBuilder<SyncWorker>()
            .setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST)
            .build()
    }
}