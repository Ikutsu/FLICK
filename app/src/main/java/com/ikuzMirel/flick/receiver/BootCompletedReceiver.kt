package com.ikuzMirel.flick.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

//Unused because some issues
class BootCompletedReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == Intent.ACTION_BOOT_COMPLETED) {
//            val serviceIntent = Intent(context, NetworkService::class.java)
//            context?.startService(serviceIntent)
        }
    }
}