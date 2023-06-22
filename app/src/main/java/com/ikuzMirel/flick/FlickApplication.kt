package com.ikuzMirel.flick

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_HIGH
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Build
import android.os.IBinder
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import androidx.work.ExistingWorkPolicy
import androidx.work.WorkManager
import com.ikuzMirel.flick.data.repositories.PreferencesRepository
import com.ikuzMirel.flick.data.service.NetworkService
import com.ikuzMirel.flick.worker.SyncWorker
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltAndroidApp
class FlickApplication: Application(), Configuration.Provider{

    private lateinit var mService: NetworkService

    @Inject
    lateinit var preferencesRepository: PreferencesRepository

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    private val connection = object : ServiceConnection {

        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            val binder = service as NetworkService.LocalBinder
            mService = binder.getService()
        }

        override fun onServiceDisconnected(arg0: ComponentName) {
        }
    }


    override fun onCreate() {
        super.onCreate()
//        val receiver = BootCompletedReceiver()
//        val filter = IntentFilter()
//        filter.addAction(Intent.ACTION_BOOT_COMPLETED)
//        registerReceiver(receiver, filter)
//        if (preferencesRepository.getIsFirstTime())
        val serviceIntent = Intent(this, NetworkService::class.java)
        startService(serviceIntent)
        Intent(this, NetworkService::class.java).also { intent ->
            bindService(intent, connection, BIND_AUTO_CREATE)
        }
        runBlocking {
            if (preferencesRepository.getJwt().isNotBlank()) startSync()
        }
        createNotificationChannel()
    }



    private fun createNotificationChannel(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                "flick_channel",
                "Flick",
                IMPORTANCE_HIGH
            )
            notificationChannel.description = "A channel for Flick chat notifications"
            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }

    private fun startSync(){
        WorkManager.getInstance(this).apply {
            enqueueUniqueWork(
                "sync",
                ExistingWorkPolicy.KEEP,
                SyncWorker.startWork()
            )
        }
    }

    override fun getWorkManagerConfiguration(): Configuration =
        Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()
}