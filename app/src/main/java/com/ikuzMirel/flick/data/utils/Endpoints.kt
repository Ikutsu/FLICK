package com.ikuzMirel.flick.data.utils

import com.ikuzMirel.flick.BuildConfig

const val REMOTE_IP = BuildConfig.ServerIP
const val REMOTE_URL = "http://$REMOTE_IP"
const val ENDPOINT_LOGIN = "$REMOTE_URL/signIn"
const val ENDPOINT_REGISTER = "$REMOTE_URL/signUp"
const val ENDPOINT_AUTH = "$REMOTE_URL/authenticate"
const val ENDPOINT_USER_INFO = "$REMOTE_URL/user"
const val ENDPOINT_USER_FRIENDS = "$REMOTE_URL/user/friends"
const val ENDPOINT_CHAT_MESSAGES = "$REMOTE_URL/messages"
const val ENDPOINT_FRIEND_REQUEST_SEND = "$REMOTE_URL/friendRequests/send"
const val ENDPOINT_FRIEND_REQUEST_ACCEPT = "$REMOTE_URL/friendRequests/accept/"
const val ENDPOINT_FRIEND_REQUEST_REJECT = "$REMOTE_URL/friendRequests/decline/"
const val ENDPOINT_FRIEND_REQUESTS_RECEIVED = "$REMOTE_URL/friendRequests/received/"
const val ENDPOINT_FRIEND_REQUESTS_SENT = "$REMOTE_URL/friendRequests/sent/"
const val ENDPOINT_WEBSOCKET = "ws://$REMOTE_IP/WSocket"
