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
import com.ikuzMirel.flick.data.response.BasicResponse
import com.ikuzMirel.flick.data.room.dao.FriendDao
import com.ikuzMirel.flick.data.room.dao.FriendReqDao
import com.ikuzMirel.flick.data.room.dao.MessageDao
import com.ikuzMirel.flick.domain.entities.FriendEntity
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

        val syncSuccess = awaitAll(
            async { fetchFriendListAndUpdateDB() },
            async { fetchMessagesAndUpdateDB() },
            async { fetchFriendRequestsAndUpdateDB() }
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

    private suspend fun fetchFriendListAndUpdateDB(): Boolean {
        var success = false
        val response = userRepository.getUserFriends().first()
        val userId = preferencesRepository.getValue(PreferencesRepository.USERID)!!
        if (response is BasicResponse.Success) {
            response.data?.friends?.forEach {
                val friendEntity = FriendEntity(
                    userId = it.userId,
                    username = it.username,
                    collectionId = it.collectionId,
                    friendWith = userId
                )
                friendDao.upsertFriend(friendEntity)
            }
            success = true
        }
        return success
    }

    private suspend fun fetchMessagesAndUpdateDB(): Boolean {
        var success = false
        val cids = friendDao.getAllFriendsCIDs().first()
        if (cids.isEmpty()) {
            success = true
        } else {
            for (cid in cids) {
                val result = chatRepository.getChatMassages(cid).first()
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

    private suspend fun fetchFriendRequestsAndUpdateDB(): Boolean {
        var success = false
        val receivedReq = friendReqRepository.getAllReceivedFriendRequests().first()
        val sentReq = friendReqRepository.getAllSentFriendRequests().first()
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