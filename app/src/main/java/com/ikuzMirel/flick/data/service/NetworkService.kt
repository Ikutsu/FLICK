package com.ikuzMirel.flick.data.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.ikuzMirel.flick.data.remote.websocket.WebSocketService
import com.ikuzMirel.flick.data.repositories.PreferencesRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import javax.inject.Inject

@AndroidEntryPoint
class NetworkService: Service(){

    @Inject
    lateinit var webSocketService: WebSocketService

    @Inject
    lateinit var preferencesRepository: PreferencesRepository

    @OptIn(DelicateCoroutinesApi::class)
    override fun onCreate() {
        super.onCreate()
        if (webSocketService.checkConnection()){
            return
        }
        GlobalScope.launch {
            if (preferencesRepository.getJwt().data != null && preferencesRepository.getUserId().data != null){
                webSocketService.connectToSocket(
                    preferencesRepository.getUserId().data!!,
                    preferencesRepository.getJwt().data!!
                )
            }
            println("Service created")
        }
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_STICKY
    }

    @OptIn(DelicateCoroutinesApi::class)
    override fun onDestroy() {
        super.onDestroy()
        GlobalScope.launch {
            webSocketService.disconnectFromSocket()
            println("Service destroyed")
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    override fun onTaskRemoved(rootIntent: Intent?) {
        super.onTaskRemoved(rootIntent)
        GlobalScope.launch {
            webSocketService.disconnectFromSocket()
            println("Service destroyed")
        }
    }
}