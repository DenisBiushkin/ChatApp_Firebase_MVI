package com.example.unmei.presentation.chat_list_feature.model

import androidx.compose.ui.graphics.painter.Painter

data class ChatListItem(
    val messageStatus: MessageStatus =MessageStatus.None,
    val notificationMessageStatus: NotificationMessageStatus = NotificationMessageStatus.On,
    val isOnline:Boolean = false,
    val fullName:String,
    val painterUser: Painter,
    val messageText:String,
    val timeStamp:Long
)