package com.example.unmei.domain.model

data class RoomDetail(
    val roomId:String,//куда отправить сообщение
    val roomIconUrl:String,//показать иконку чата
    val typeRoom: TypeRoom,//какой deeplink

    //подумать на счет групповых
    //т.к нужны toToken всех участников
    val senderId:String,//получить to token
    val senderFullName:String,
    val senderIconUrl:String? = null,//если это чат показать рядом иконку отправителя
    val message: Message,//maybe String

)
