package com.ikuzMirel.flick.worker

import android.content.Context
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkerParameters
import com.ikuzMirel.flick.R
import com.ikuzMirel.flick.data.remote.websocket.WebSocketApi
import com.ikuzMirel.flick.data.repositories.PreferencesRepository
import com.ikuzMirel.flick.data.repositories.UserRepository
import com.ikuzMirel.flick.data.response.BasicResponse
import com.ikuzMirel.flick.data.room.dao.FriendDao
import com.ikuzMirel.flick.data.room.dao.FriendReqDao
import com.ikuzMirel.flick.data.room.dao.MessageDao
import com.ikuzMirel.flick.domain.entities.FriendRequestEntity
import com.ikuzMirel.flick.domain.entities.FriendRequestStatus
import com.ikuzMirel.flick.domain.entities.MessageEntity
import com.ikuzMirel.flick.service.NotificationService
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit

@HiltWorker
class WebSocketWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val webSocketApi: WebSocketApi,
    private val preferencesRepository: PreferencesRepository,
    private val messageDao: MessageDao,
    private val friendDao: FriendDao,
    private val friendReqDao: FriendReqDao,
    private val userRepository: UserRepository,
    private val notificationService: NotificationService
) : CoroutineWorker(context, params) {

    val notification = NotificationCompat.Builder(context, "flick_channel")
        .setContentTitle("Worker notification")
        .setContentText("Worker notification")
        .setSmallIcon(R.drawable.ic_launcher_foreground)
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        .build()

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        println("WebSocketWorker: doWork")
        if (webSocketApi.checkConnection()) {
            return@withContext Result.success()
        }
        val userId = preferencesRepository.getUserId()
        val jwt = preferencesRepository.getJwt()
        if (userId.isBlank() && jwt.isBlank()) {
            return@withContext Result.failure()
        }

        val connection = webSocketApi.connectToSocket(userId, jwt)
        println("WebSocketWorker: connection: $connection")
        if (connection is BasicResponse.Success) {
            receiveIncomingMessages()
            return@withContext Result.retry()
        } else {
            Log.d("NetworkService", "Error connecting to socket")
            return@withContext Result.retry()
        }
    }

    override suspend fun getForegroundInfo(): ForegroundInfo {
        return ForegroundInfo(3, notification)
    }

    private suspend fun receiveIncomingMessages() {
        val myUserId = preferencesRepository.getUserId()
        webSocketApi.receiveMessage().collect { message ->
            println(message.type)
            println(message.data)
            when (message.type) {
                "chatMessage" -> {
                    messageDao.upsertMessage(message.data as MessageEntity)
                    if (message.data.senderUid != myUserId) {
                        notificationService.showChatNotification(message.data)
                    }
                }

                "friendRequest" -> {
                    val friendReq = message.data as FriendRequestEntity
                    when (friendReq.status) {
                        FriendRequestStatus.ACCEPTED.name -> {
                            val newFriend = userRepository.getUserFriend(
                                myUserId,
                                message.data.receiverId,
                                preferencesRepository.getJwt()
                            ).first()
                            if (newFriend is BasicResponse.Success) {
                                friendDao.upsertFriend(newFriend.data!!)
                                notificationService.showFriendRequestNotification(friendReq)
                            }
                        }

                        FriendRequestStatus.REJECTED.name, FriendRequestStatus.PENDING.name -> {
                            friendReqDao.upsertFriendReq(friendReq)
                            notificationService.showFriendRequestNotification(friendReq)
                        }

                        FriendRequestStatus.CANCELLED.name -> {
                            friendReqDao.deleteFriendReq(friendReq.id)
                        }
                    }
                }
            }
        }
    }

    companion object {
        const val WORK_NAME = "WebSocketWorker"
        fun startWork() = PeriodicWorkRequestBuilder<WebSocketWorker>(15, TimeUnit.MINUTES)
            .addTag(WORK_NAME)
            .build()
    }

}