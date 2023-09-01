package com.ikuzMirel.flick.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkerParameters
import com.ikuzMirel.flick.data.remote.websocket.LastReadMessageSet
import com.ikuzMirel.flick.data.remote.websocket.WebSocketApi
import com.ikuzMirel.flick.data.remote.websocket.WebSocketMessage
import com.ikuzMirel.flick.data.response.BasicResponse
import com.ikuzMirel.flick.data.room.dao.FriendDao
import com.ikuzMirel.flick.handler.LastMessageQueueHandler
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.util.concurrent.TimeUnit

@HiltWorker
class UnreadMessageWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val webSocketApi: WebSocketApi,
    private val friendDao: FriendDao,
    private val lastMessageQueueHandler: LastMessageQueueHandler
): CoroutineWorker(context, params) {

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
            if (!webSocketApi.checkConnection()) {
                return@withContext Result.retry()
            } else {
                sendMessage()
                return@withContext Result.retry()
            }
        }

    private suspend fun sendMessage() {
        while (true) {
            if (lastMessageQueueHandler.getReadMessageQueue().isEmpty()) {
                delay(1000)
                continue
            }
            val websocketMessage = WebSocketMessage(
                "lastReadMessage",
                LastReadMessageSet(lastMessageQueueHandler.getReadMessageQueue())
            )

            val json = Json.encodeToString(websocketMessage)
            val response = webSocketApi.sendMessage(json)
            if (response is BasicResponse.Success) {
                lastMessageQueueHandler.getReadMessageQueue().forEach {
                    friendDao.updateLastReadMessageTime(it.friendUserId, it.lastReadMessageTime)
                }
                lastMessageQueueHandler.clearReadMessageQueue()
                delay(1000)
                continue
            } else {
                break
            }
        }
    }
    companion object {

        fun startWork() = PeriodicWorkRequestBuilder<UnreadMessageWorker>(15, TimeUnit.MINUTES)
            .addTag(WORK_NAME)
            .build()

        const val WORK_NAME = "UnreadMessageWorker"
    }
}