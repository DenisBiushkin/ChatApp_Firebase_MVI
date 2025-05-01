package com.example.unmei.presentation.singleChat_feature.model

import com.example.unmei.domain.model.messages.Attachment
import com.example.unmei.domain.model.messages.Attachment.Audio
import com.example.unmei.domain.model.messages.Attachment.File
import com.example.unmei.domain.model.messages.Attachment.Image
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

    val attachmentsUi: Map<String,AttachmentUi> = emptyMap()
){

}
