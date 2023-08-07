package com.ikuzMirel.flick.data.constants

import com.ikuzMirel.flick.BuildConfig

const val REMOTE_IP = BuildConfig.serverIP
const val REMOTE_URL = "https://$REMOTE_IP"
const val ENDPOINT_LOGIN = "$REMOTE_URL/signIn"
const val ENDPOINT_REGISTER = "$REMOTE_URL/signUp"
const val ENDPOINT_AUTH = "$REMOTE_URL/authenticate"
const val ENDPOINT_USER_INFO = "$REMOTE_URL/user"
const val ENDPOINT_USER_FRIENDS = "$REMOTE_URL/user/friends"
const val ENDPOINT_USER_FRIEND = "$REMOTE_URL/user/friend"
const val ENDPOINT_USER_SEARCH = "$REMOTE_URL/user/search"
const val ENDPOINT_CHAT_MESSAGES = "$REMOTE_URL/messages"
const val ENDPOINT_FRIEND_REQUEST_SEND = "$REMOTE_URL/friendRequests/send"
const val ENDPOINT_FRIEND_REQUEST_ACCEPT = "$REMOTE_URL/friendRequests/accept"
const val ENDPOINT_FRIEND_REQUEST_CANCEL = "$REMOTE_URL/friendRequests/cancel"
const val ENDPOINT_FRIEND_REQUEST_REJECT = "$REMOTE_URL/friendRequests/reject"
const val ENDPOINT_FRIEND_REQUESTS_RECEIVED = "$REMOTE_URL/friendRequests/received"
const val ENDPOINT_FRIEND_REQUESTS_SENT = "$REMOTE_URL/friendRequests/sent"
const val ENDPOINT_WEBSOCKET = "wss://$REMOTE_IP/websocket"
