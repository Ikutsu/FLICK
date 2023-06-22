package com.ikuzMirel.flick.service

import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.net.toUri
import com.ikuzMirel.flick.MainActivity
import com.ikuzMirel.flick.R
import com.ikuzMirel.flick.data.room.dao.FriendDao
import com.ikuzMirel.flick.domain.entities.MessageEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject

class NotificationService @Inject constructor(
    private val context: Context,
    private val friendDao: FriendDao
) {

    private val serviceJob = SupervisorJob()
    private val serviceScope = CoroutineScope(Dispatchers.IO + serviceJob)

    private val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    fun showChatNotification(data: MessageEntity) {
        serviceScope.launch {
            friendDao.getFriend(data.senderUid).collect{ friend ->
                val deeplinkIntent = Intent(
                    Intent.ACTION_VIEW,
                    "https://flick.com/chat/${friend.username}/${friend.userId}/${friend.collectionId}".toUri(),
                    context,
                    MainActivity::class.java
                )
                val pending: PendingIntent = TaskStackBuilder.create(context).run {
                    addNextIntentWithParentStack(deeplinkIntent)
                    getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
                }

                val notification = NotificationCompat.Builder(context, "flick_channel")
                    .setContentTitle(friend.username)
                    .setContentText(data.content)
                    .setSmallIcon(R.drawable.ic_launcher_foreground)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setContentIntent(pending)
                    .build()

                notificationManager.notify(1, notification)
            }
        }
    }
}