package com.example.unmei.domain.model.messages

import android.util.Log
import com.example.unmei.domain.model.TypeMessageResp
import com.example.unmei.domain.model.UserExtended
import com.example.unmei.domain.model.messages.Attachment.Audio
import com.example.unmei.domain.model.messages.Attachment.File
import com.example.unmei.domain.model.messages.Attachment.Image
import com.example.unmei.presentation.chat_list_feature.model.MessageStatus
import com.example.unmei.presentation.singleChat_feature.model.AttachmentTypeUI
import com.example.unmei.presentation.singleChat_feature.model.AttachmentUi
import com.example.unmei.presentation.singleChat_feature.model.MessageListItemUI
import com.example.unmei.presentation.singleChat_feature.model.MessageType
import com.example.unmei.util.ConstansDev.TAG
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

data class Message(
    val id:String="",
    val senderId: String,
    val timestamp:Long = 0L,
    val type: TypeMessageResp = TypeMessageResp.TEXT,
    val edited: Boolean = false,//изменено
    val readed: Boolean = false,//отправлено, прочитано
    val text: String? = null,
    val attachment: List<Attachment> ?= null,

    ){
    fun toMessageListItemUI(
        owUid:String,
        mapUsers:Map<String,UserExtended>? = null
    ):MessageListItemUI = this.run {
            MessageListItemUI(
                    text = text ?: "",
                    messageId = id,
                    timestamp = timestampToLocalDateTime(timestamp),
                    timeString = timestampToStringHourMinute(timestamp),
                    isChanged = edited,
                    isOwn = owUid == senderId,
                    fullName = mapUsers?.get(senderId)?.user?.fullName ?: "Undefined" ,//брать из hAshMap????? ДА)))))))
                    type = when(type){
                            TypeMessageResp.TEXT -> MessageType.Text
                            TypeMessageResp.ONLYPHOTO -> MessageType.OnlyImage
                            TypeMessageResp.WITHANYATTACHMENT -> MessageType.Text
                    },
                    attachmentsUi = attachment?.map {
                        val attacmentUi=toAttachmentUI(it)
                        attacmentUi.uploadedUrl!! to attacmentUi
                   }?.toMap() ?: emptyMap()
                ,
                    status =if(readed) MessageStatus.Readed else MessageStatus.Send
            )
    }
    private fun toAttachmentUI(attachment: Attachment): AttachmentUi {
        val type = when (attachment) {
            is Audio -> {
                AttachmentTypeUI.AUDIO
            }

            is File -> {
                AttachmentTypeUI.FILE
            }

            is Image -> {
                AttachmentTypeUI.IMAGE
            }
        }
        return AttachmentUi(
            isLoading = false,
            uploadedUrl = attachment.attachUrl,
            type = type
        )
    }

    private fun timestampToStringHourMinute(timestamp: Long):String{
            val formatter = DateTimeFormatter.ofPattern("HH:mm")
                    .withZone(ZoneId.systemDefault())
            return formatter.format(Instant.ofEpochMilli(timestamp))
    }
    private fun timestampToLocalDateTime(timestamp:Long):LocalDateTime{
          return Instant.ofEpochMilli(timestamp)
                    .atZone(ZoneId.systemDefault())  // Использует системный часовой пояс
                    .toLocalDateTime()
    }
}

//val id:String="",
//val senderId: String = "",//отправитель
//val text: String = "",
//val timestamp:Long = 0L,//время создания
//val type: String ="",//image,file,text,audio
//val readed: Boolean = false,//отправлено, прочитано
//val mediaUrl: String = "",
//val edited: Boolean = false,//изменено