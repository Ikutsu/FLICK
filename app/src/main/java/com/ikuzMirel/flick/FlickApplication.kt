package com.ikuzMirel.flick

import android.app.Application
import android.content.Intent
import android.content.IntentFilter
import com.ikuzMirel.flick.data.service.NetworkService
import com.ikuzMirel.flick.receiver.BootCompletedReceiver
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class FlickApplication: Application(){
    override fun onCreate() {
        super.onCreate()
//        val receiver = BootCompletedReceiver()
//        val filter = IntentFilter()
//        filter.addAction(Intent.ACTION_BOOT_COMPLETED)
//        registerReceiver(receiver, filter)
        startService(Intent(this, NetworkService::class.java))
    }
}