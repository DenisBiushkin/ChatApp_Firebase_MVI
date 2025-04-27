package com.example.unmei.data.model

import com.example.unmei.domain.model.TypeRoom

data class ChatRoomResponseAdvence(
        val id :String ="",
        val type: String="",
        val chatName:String = "",
        val iconUrl:String = "",
        val timestamp:Any? = null,
        val moderators: Map<String,Boolean> = emptyMap(),//для группового чата
        val members:Map<String,Boolean>  = emptyMap(),
        //мб и не надо
        //active users прямо в приложении
        val activeUsers:Map<String,Boolean>  = emptyMap(),
){
        fun toChatRoomAdvence():ChatRoomAdvance = this.run {
                ChatRoomAdvance(
                        id=id,
                        type = when(type){
                                "private" -> TypeRoom.PRIVATE
                                else ->TypeRoom.PUBLIC
                        },
                        chatName = chatName,
                        iconUrl = iconUrl,
                        timestamp =  if(timestamp is Long) timestamp else 0L,
                        moderators = moderators.keys.toSet(),
                        members = members.keys.toSet(),

                        activeUsers =activeUsers.keys.toSet()
                )
        }
}

data class ChatRoomAdvance(
        val id:String,
        val type: TypeRoom,
        val chatName: String,
        val iconUrl: String,
        val timestamp: Long,
        val moderators: Set<String> = emptySet(),
        val members:  Set<String> ,

        val activeUsers: Set<String> = emptySet()
)