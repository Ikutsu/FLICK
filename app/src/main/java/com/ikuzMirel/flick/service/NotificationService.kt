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
import com.ikuzMirel.flick.data.repositories.PreferencesRepository
import com.ikuzMirel.flick.data.room.dao.FriendDao
import com.ikuzMirel.flick.domain.entities.FriendRequestEntity
import com.ikuzMirel.flick.domain.entities.FriendRequestStatus
import com.ikuzMirel.flick.domain.entities.MessageEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

class NotificationService @Inject constructor(
    private val context: Context,
    private val friendDao: FriendDao,
    private val preferencesRepository: PreferencesRepository
) {

    private val serviceJob = SupervisorJob()
    private val serviceScope = CoroutineScope(Dispatchers.IO + serviceJob)

    private val notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    fun showChatNotification(data: MessageEntity) {
        serviceScope.launch {
            val myUid = preferencesRepository.getValue(PreferencesRepository.USERID) ?: return@launch
            val friend = friendDao.getFriend(data.senderUid, myUid).first()
            val deeplinkIntent = Intent(
                Intent.ACTION_VIEW,
                "https://flick.com/chat/${friend.username}/${friend.userId}/${friend.collectionId}".toUri(),
                context,
                MainActivity::class.java
            )
            val pending: PendingIntent = TaskStackBuilder.create(context).run {
                addNextIntentWithParentStack(deeplinkIntent)
                getPendingIntent(
                    0,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )
            }

            val notification = NotificationCompat.Builder(context, "flick_msg_channel")
                .setContentTitle(friend.username)
                .setContentText(data.content)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setContentIntent(pending)
                .build()

            notificationManager.notify(1, notification)
        }
    }

    fun showFriendRequestNotification(friendRequest: FriendRequestEntity) {
        serviceScope.launch {

            val deeplinkIntent = when (friendRequest.status) {
                FriendRequestStatus.PENDING.name -> {
                    Intent(
                        Intent.ACTION_VIEW,
                        "https://flick.com/FriendRequest".toUri(),
                        context,
                        MainActivity::class.java
                    )
                }
                FriendRequestStatus.ACCEPTED.name -> {
                    Intent(
                        Intent.ACTION_VIEW,
                        "https://flick.com/chat/${friendRequest.receiverName}/${friendRequest.receiverId}/${
                            friendDao.getFriend(
                                friendRequest.receiverId,
                                friendRequest.senderId
                            ).first().collectionId
                        }".toUri(),
                        context,
                        MainActivity::class.java
                    )
                }
                else -> {
                    Intent(
                        Intent.ACTION_VIEW,
                        "https://flick.com/home".toUri(),
                        context,
                        MainActivity::class.java
                    )
                }
            }
            val pending: PendingIntent = TaskStackBuilder.create(context).run {
                addNextIntentWithParentStack(deeplinkIntent)
                getPendingIntent(
                    0,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )
            }

            val contextTitle = when (friendRequest.status) {
                FriendRequestStatus.PENDING.name -> "New friend request"
                FriendRequestStatus.ACCEPTED.name -> "New friend"
                FriendRequestStatus.REJECTED.name -> "Friend request rejected"
                else -> ""
            }

            val contentText = when (friendRequest.status) {
                FriendRequestStatus.PENDING.name -> "You have a new friend request"
                FriendRequestStatus.ACCEPTED.name -> "You are now friends with ${friendRequest.receiverName}"
                FriendRequestStatus.REJECTED.name -> "Your friend request was rejected"
                else -> ""
            }

            val notification = NotificationCompat.Builder(context, "flick_FR_channel")
                .setContentTitle(contextTitle)
                .setContentText(contentText)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setContentIntent(pending)
                .build()

            notificationManager.notify(2, notification)
        }
    }
}