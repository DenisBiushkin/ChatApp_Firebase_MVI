package com.example.unmei.presentation.conversation_future.model

import com.example.unmei.domain.model.messages.Attachment
import com.example.unmei.presentation.chat_list_feature.model.MessageStatus
import java.time.LocalDateTime

data class MessageListItemUI(
    val text:String,
    val messageId:String = "",
    val timestamp: LocalDateTime,
    val timeString :String = "",
    val isChanged: Boolean= false,
    val isOwn: Boolean = false,
    val fullName: String,
    val status: MessageStatus = MessageStatus.Send,
    val type: MessageType = MessageType.Text,
    val visvilityOptins:Boolean = false,
    val attachments: List<Attachment>? = null
)
