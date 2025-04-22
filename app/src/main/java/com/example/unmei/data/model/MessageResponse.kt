package com.example.unmei.data.model

import android.util.Log
import com.example.unmei.domain.model.Message
import com.example.unmei.domain.model.TypeMessageResp
import com.example.unmei.domain.model.Attachment
import com.example.unmei.util.ConstansDev.TAG
import com.google.firebase.database.ServerValue
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json


data class MessageResponse(
    //100% поля которые есть во всех сообщениях,независимо от типа
    val id:String="",
    val senderId: String = "",//отправитель
    val timestamp:Any?=null,//время создания
    val type: String ="",//textOnly/withattachments/
    val edited: Boolean = false,//изменено
    //мб надо будет изменить
    val readed: Boolean = false,//отправлено, прочитано
    val text: String? = null,
    val attachment: String? = null,
){
    fun toMessage():Message =this.run {


        Message(
            id=id,
            senderId =senderId,
            timestamp = when(timestamp){
                is Long -> timestamp
                else -> 0L
            },
            type=when(type){
                    "text" -> TypeMessageResp.TEXT
                    "onlyphoto"-> TypeMessageResp.ONLYPHOTO
                    else -> TypeMessageResp.WITHANYATTACHMENT
            },
            readed=readed,
            edited=edited,
            text = text,
            attachment = if (!attachment.isNullOrEmpty()) Json.decodeFromString<List<Attachment>>(attachment) else emptyList()

        )
    }
     fun fromMessageToResp(
         message: Message
     ):MessageResponse{
         var typeMessage:String = "text"
        // var attachmentRed = mutableMapOf<String,Any>()
         if(!message.attachment.isNullOrEmpty()){
             typeMessage = "withanyattachment"
             val onlyPhoto = message.attachment.all { it is Attachment.Image}
             if (onlyPhoto){
                 typeMessage = "onlyhoto"
             }

         }
        // Log.d(TAG,attachmentRed.toString())
         return MessageResponse(
             senderId= message.senderId,
             text= message.text,
             type = typeMessage,
             timestamp = ServerValue.TIMESTAMP,
             attachment =  if(!message.attachment.isNullOrEmpty()) Json.encodeToString(message.attachment) else null
         )
     }
}


