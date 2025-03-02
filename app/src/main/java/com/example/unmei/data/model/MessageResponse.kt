package com.example.unmei.data.model

import com.example.unmei.domain.model.Message

data class MessageResponse(
    val id:String="",
    val senderId: String = "",//отправитель
    val text: String = "",
    val timestamp:Any?=null,//время создания
    val type: String ="",//image,file,text,audio
    val readed: Boolean = false,//отправлено, прочитано
    val mediaUrl: String = "",
    val edited: Boolean = false,//изменено
){
    fun toMessage():Message =this.run {
        Message(
            id=id,
            senderId =senderId,
            text=text,
            timestamp = when(timestamp){
                is Long -> timestamp
                else -> 0L
            },
            type=type,
            readed=readed,
            mediaUrl=mediaUrl,
            edited=edited
        )
    }
}


