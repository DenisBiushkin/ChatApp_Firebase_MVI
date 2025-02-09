package com.example.unmei.domain.model

data class ChatRoom(
        val id :String ="",//id группы == узлу ген Firebase
        val type: String="",//приватный или чатсный
        val timestamp:Map<String,String> = emptyMap(),//время создания
        val moderators: Map<String,Boolean> = emptyMap(),//для группового чата()
        val members:Map<String,Boolean> = emptyMap(),
        val lastMessage: Message? = null
    )