package com.ikuzMirel.flick.handler

import com.ikuzMirel.flick.data.remote.websocket.LastReadMessage
import com.ikuzMirel.flick.data.room.dao.FriendDao
import javax.inject.Inject

class LastMessageQueueHandler @Inject constructor(
    private val friendDao: FriendDao
) {

    private val readMessageQueue = mutableListOf<LastReadMessage>()
    fun addReadMessage(lastReadMessage: LastReadMessage) {
        val localTime = friendDao.getLateReadMessageTime(lastReadMessage.friendUserId)
        if (localTime > lastReadMessage.lastReadMessageTime) {
            return
        }

        if (readMessageQueue.any { it.friendUserId == lastReadMessage.friendUserId }) {
            val message = readMessageQueue.first { it.friendUserId == lastReadMessage.friendUserId }
            if (message.lastReadMessageTime < lastReadMessage.lastReadMessageTime){
                message.lastReadMessageTime = lastReadMessage.lastReadMessageTime
            }
        } else {
            readMessageQueue.add(lastReadMessage)
        }
    }

    fun getReadMessageQueue(): List<LastReadMessage> {
        return readMessageQueue
    }

    fun clearReadMessageQueue() {
        readMessageQueue.clear()
    }

}