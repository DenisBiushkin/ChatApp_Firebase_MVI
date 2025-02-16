package com.example.unmei.presentation.conversation_future.model

import androidx.compose.ui.graphics.painter.Painter
import com.example.unmei.presentation.chat_list_feature.model.MessageStatus
import com.example.unmei.presentation.chat_list_feature.model.NotificationMessageStatus

data class ChatListItemUI(
    val messageStatus: MessageStatus = MessageStatus.None,
    val notificationMessageStatus: NotificationMessageStatus = NotificationMessageStatus.On,
    val isOnline:Boolean = false,
    val fullName:String,
    val painterUser: Painter,
    val messageText:String,
    val timeStamp:Long
)