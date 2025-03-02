package com.example.unmei.domain.model

data class Message(
        val id:String="",
        val senderId: String = "",//отправитель
        val text: String = "",
        val timestamp:Long = 0L,//время создания
        val type: String ="",//image,file,text,audio
        val readed: Boolean = false,//отправлено, прочитано
        val mediaUrl: String = "",
        val edited: Boolean = false,//изменено
        )