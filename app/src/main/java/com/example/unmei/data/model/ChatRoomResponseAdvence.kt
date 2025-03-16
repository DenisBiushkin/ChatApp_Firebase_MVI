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
        val activeUsers:Map<String,Boolean>  = emptyMap(),
){
        fun toChatRoomAdvence():ChatRoomAdvence = this.run {
                ChatRoomAdvence(
                        id=id,
                        type = when(type){
                                "private" -> TypeRoom.PRIVATE
                                else ->TypeRoom.PUBLIC
                        },
                        chatName = chatName,
                        iconUrl = iconUrl,
                        timestamp =  if(timestamp is Long) timestamp else 0L,
                        moderators = moderators.keys.toList(),
                        members = members.keys.toList()
                )
        }
}

data class ChatRoomAdvence(
        val id:String,
        val type: TypeRoom,
        val chatName: String,
        val iconUrl: String,
        val timestamp: Long,
        val moderators: List<String> = emptyList(),
        val members: List<String>,
        val activeUsers: List<String> = emptyList()
)