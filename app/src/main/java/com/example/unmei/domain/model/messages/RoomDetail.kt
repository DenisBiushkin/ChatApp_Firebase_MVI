package com.example.unmei.domain.model.messages

import com.example.unmei.domain.model.TypeRoom

data class RoomDetail(
    val roomId:String,//куда отправить сообщение +deeplink
    val roomIconUrl:String,//показать иконку чата
    val roomName:String,
    val typeRoom: TypeRoom,//какой deeplink

    //forGroup
    val senderFullName:String? = null,//если это чат показать рядом иконку отправителя
    val senderIconUrl:String? = null,//если это чат показать рядом иконку отправителя
)
