package com.ikuzMirel.flick.worker

import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkerParameters
import com.ikuzMirel.flick.R
import com.ikuzMirel.flick.data.repositories.ChatRepository
import com.ikuzMirel.flick.data.repositories.FriendReqRepository
import com.ikuzMirel.flick.data.repositories.PreferencesRepository
import com.ikuzMirel.flick.data.repositories.UserRepository
import com.ikuzMirel.flick.data.requests.FriendListRequest
import com.ikuzMirel.flick.data.requests.MessageListRequest
import com.ikuzMirel.flick.data.response.BasicResponse
import com.ikuzMirel.flick.data.room.dao.FriendDao
import com.ikuzMirel.flick.data.room.dao.FriendReqDao
import com.ikuzMirel.flick.data.room.dao.MessageDao
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext

@HiltWorker
class SyncWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val preferencesRepository: PreferencesRepository,
    private val chatRepository: ChatRepository,
    private val userRepository: UserRepository,
    private val friendReqRepository: FriendReqRepository,
    private val friendDao: FriendDao,
    private val messageDao: MessageDao,
    private val friendReqDao: FriendReqDao
) : CoroutineWorker(context, params) {

    val notification = NotificationCompat.Builder(context, "flick_channel")
        .setContentTitle("Worker notification")
        .setContentText("Worker notification")
        .setSmallIcon(R.drawable.ic_launcher_foreground)
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        .build()

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        val jwt = preferencesRepository.getJwt()
        val userId = preferencesRepository.getUserId()

        if (jwt.isBlank() || userId.isBlank()) {
            Result.failure()
        }

        val syncSuccess = awaitAll(
            async { fetchFriendListAndUpdateDB(jwt, userId) },
            async { fetchMessagesAndUpdateDB(jwt) },
            async { fetchFriendRequestsAndUpdateDB(jwt) }
        ).all { it }

        if (syncSuccess) {
            Result.success()
        } else {
            Result.retry()
        }
    }

    override suspend fun getForegroundInfo(): ForegroundInfo {
        return ForegroundInfo(3, notification)
    }

    private suspend fun fetchFriendListAndUpdateDB(jwt: String, userId: String): Boolean {
        var success = false
        val response = userRepository.getUserFriends(FriendListRequest(userId, jwt)).first()
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

    private suspend fun fetchFriendRequestsAndUpdateDB(jwt: String): Boolean {
        var success = false
        val receivedReq = friendReqRepository.getAllReceivedFriendRequests(jwt).first()
        val sentReq = friendReqRepository.getAllSentFriendRequests(jwt).first()
        if (receivedReq is BasicResponse.Success) {
            receivedReq.data?.friendRequests?.forEach {
                friendReqDao.upsertFriendReq(it)
            }
        }
        if (sentReq is BasicResponse.Success) {
            sentReq.data?.friendRequests?.forEach {
                friendReqDao.upsertFriendReq(it)
            }
            success = true
        }
        return success
    }

    companion object {
        const val WORK_NAME = "SyncWorker"
        fun startWork() = OneTimeWorkRequestBuilder<SyncWorker>()
            .addTag(WORK_NAME)
            .build()
    }
}