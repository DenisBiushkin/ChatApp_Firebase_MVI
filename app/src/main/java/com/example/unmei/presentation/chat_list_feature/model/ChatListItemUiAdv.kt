package com.example.unmei.presentation.chat_list_feature.model

import com.example.unmei.domain.model.messages.Message
import com.example.unmei.domain.model.TypeRoom


data class ChatListItemUiAdv(
     val chatId: String,
     val chatName:String,//ок   ок-необходим непосредственно для отрисовки
     val iconUrl:String,//ок
     val isOnline: Boolean= false,//ок
     val notificationStatus: NotificationMessageStatus = NotificationMessageStatus.On,//ок
     val typingStatus: TypingStatus=TypingStatus.None,
     val typeChat: TypeRoom,//ок
     val timestamp: Long,
     val contentMessage: ContentMessage?,//ок
     val lastMessageTimeString:String ="",
     val members: Set<String>,
     val moderators: Set<String>? = null,
     val unreadedCountMessage: Int,//ок,

     val messageStatus:MessageStatus = MessageStatus.Send
)
data class ContentMessage(
     val contentSender:String,
     val message: Message,
)

sealed class TypingStatus{

     object None :TypingStatus()

     data class Typing(
          val whoTyping: String
     ):TypingStatus()
}