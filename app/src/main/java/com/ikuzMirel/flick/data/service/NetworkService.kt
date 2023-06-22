package com.ikuzMirel.flick.data.service

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.Log
import com.ikuzMirel.flick.data.remote.websocket.WebSocketApi
import com.ikuzMirel.flick.data.repositories.ChatRepository
import com.ikuzMirel.flick.data.repositories.PreferencesRepository
import com.ikuzMirel.flick.data.repositories.UserRepository
import com.ikuzMirel.flick.data.response.BasicResponse
import com.ikuzMirel.flick.data.room.dao.FriendDao
import com.ikuzMirel.flick.data.room.dao.MessageDao
import com.ikuzMirel.flick.domain.entities.MessageEntity
import com.ikuzMirel.flick.service.NotificationService
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class NetworkService : Service() {

    @Inject
    lateinit var webSocketApi: WebSocketApi

    @Inject
    lateinit var preferencesRepository: PreferencesRepository

    @Inject
    lateinit var messageDao: MessageDao

    @Inject
    lateinit var friendDao: FriendDao

    @Inject
    lateinit var userRepository: UserRepository

    @Inject
    lateinit var chatRepository: ChatRepository

    @Inject
    lateinit var notificationService: NotificationService

    private val serviceJob = SupervisorJob()
    private val serviceScope = CoroutineScope(Dispatchers.IO + serviceJob)

    private val binder = LocalBinder()

    inner class LocalBinder : Binder() {
        fun getService(): NetworkService = this@NetworkService
    }


    override fun onCreate() {
        super.onCreate()
        serviceScope.launch {
            if (webSocketApi.checkConnection()) {
                webSocketApi.disconnectFromSocket()
            }
            val userId = preferencesRepository.getUserId()
            val jwt = preferencesRepository.getJwt()
            if (userId.isBlank() && jwt.isBlank()) {
                return@launch
            }
            webSocketApi.connectToSocket(userId, jwt).let { responseResult ->
                if (responseResult is BasicResponse.Success) {
                    receiveIncomingMessages()
                } else {
                    Log.d("NetworkService", "Error connecting to socket")
                }
            }
        }
        Log.d("NetworkService", "Service created")
    }

    override fun onBind(intent: Intent?): IBinder {
        return binder
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d("NetworkService", "Service started")
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceScope.launch {
            webSocketApi.disconnectFromSocket()
            Log.d("NetworkService", "Service destroyed")
        }
        serviceJob.cancel()
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        super.onTaskRemoved(rootIntent)
        serviceScope.launch {
            webSocketApi.disconnectFromSocket()
            Log.d("NetworkService", "Service destroyed on task removed")
        }
        serviceJob.cancel()
    }

    private suspend fun receiveIncomingMessages() {
        val myUserId = preferencesRepository.getUserId()
        webSocketApi.receiveMessage().collect { message ->
            when (message.type) {
                "chatMessage" -> {
                    messageDao.upsertMessage(message.data as MessageEntity)
                    if (message.data.senderUid != myUserId) {
                        notificationService.showChatNotification(message.data)
                    }
                }

                "friendRequest" -> {
                    TODO("Not yet implemented")
                }
            }
        }
    }
}