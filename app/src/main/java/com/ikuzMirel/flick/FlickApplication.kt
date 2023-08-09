package com.ikuzMirel.flick

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_HIGH
import android.os.Build
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.WorkManager
import com.ikuzMirel.flick.data.repositories.PreferencesRepository
import com.ikuzMirel.flick.worker.SyncWorker
import com.ikuzMirel.flick.worker.WebSocketWorker
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltAndroidApp
class FlickApplication: Application(), Configuration.Provider{


    @Inject
    lateinit var preferencesRepository: PreferencesRepository

    @Inject
    lateinit var workerFactory: HiltWorkerFactory


    override fun onCreate() {
        super.onCreate()
//        val receiver = BootCompletedReceiver()
//        val filter = IntentFilter()
//        filter.addAction(Intent.ACTION_BOOT_COMPLETED)
//        registerReceiver(receiver, filter)
//        if (preferencesRepository.getIsFirstTime())
        runBlocking {
            preferencesRepository.getValue(PreferencesRepository.TOKEN) ?: return@runBlocking
            startSyncAndWebSocket()
        }
        createNotificationChannel()
    }



    private fun createNotificationChannel(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

            val messageNotificationChannel = NotificationChannel(
                "flick_msg_channel",
                "Flick",
                IMPORTANCE_HIGH
            )
            messageNotificationChannel.description = "A channel for Flick chat notifications"
            notificationManager.createNotificationChannel(messageNotificationChannel)

            val friendReqNotificationChannel = NotificationChannel(
                "flick_FR_channel",
                "Flick",
                IMPORTANCE_HIGH
            )
            friendReqNotificationChannel.description = "A channel for Flick friend request notifications"
            notificationManager.createNotificationChannel(friendReqNotificationChannel)
        }
    }

    private fun startSyncAndWebSocket(){
        WorkManager.getInstance(this).apply {
            enqueueUniqueWork(
                SyncWorker.WORK_NAME,
                ExistingWorkPolicy.KEEP,
                SyncWorker.startWork()
            )
            enqueueUniquePeriodicWork(
                WebSocketWorker.WORK_NAME,
                ExistingPeriodicWorkPolicy.CANCEL_AND_REENQUEUE,
                WebSocketWorker.startWork()
            )
        }
    }

    override fun getWorkManagerConfiguration(): Configuration =
        Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()
}