package com.example.unmei.domain.model

import com.example.unmei.presentation.chat_list_feature.model.MessageStatus
import com.example.unmei.presentation.conversation_future.model.MessageListItemUI
import com.example.unmei.presentation.conversation_future.model.MessageType
import java.time.Instant
import java.time.ZoneOffset
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
        fun toMessageListItemUI(owUid:String):MessageListItemUI = this.run {
                MessageListItemUI(
                        text = text ?: "",
                        messageId = id,
                        timestamp = timestamp,
                        timeString = TimeStampToString(timestamp),
                        isChanged = edited,
                        isOwn = owUid == senderId,
                        fullName = "name",//брать из hAshMap?????
                        type = when(type){
                                TypeMessageResp.TEXT -> MessageType.Text
                                TypeMessageResp.ONLYPHOTO -> MessageType.Text
                                TypeMessageResp.WITHANYATTACHMENT -> MessageType.Text
                        },
                        attachments = attachment,
                        status =if(readed) MessageStatus.Readed else MessageStatus.Send
                )
        }
        private fun TimeStampToString(timestamp: Long):String{
                val formatter = DateTimeFormatter.ofPattern("HH:mm")
                        .withZone(ZoneOffset.UTC)
                return formatter.format(Instant.ofEpochMilli(timestamp))
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