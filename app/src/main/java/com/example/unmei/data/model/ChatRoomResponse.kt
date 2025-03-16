package com.example.unmei.data.model

data class ChatRoomResponse(
        val id :String ="",//id группы == узлу ген Firebase
        val type: String="",//приватный или чатсный
        val timestamp:Any? = null,//время создания
        val moderators: Map<String,Boolean> = emptyMap(),//для группового чата()
        val members:Map<String,Boolean> = emptyMap(),
        val lastMessage: String? = null
    )



