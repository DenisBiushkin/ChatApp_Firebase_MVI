package com.example.unmei.domain.model.messages

import com.example.unmei.presentation.chat_list_feature.model.ChatItemUI

data class ChatRoom(
        val id :String, //id группы == узлу ген Firebase
        val type: String,//приватный или чатсный
        val timestamp:Long,//время создания
        val moderators: Map<String,Boolean> = emptyMap(),//для группового чата()
        val members:Map<String,Boolean> = emptyMap(),
        val lastMessage: String? = null,

        //новое
     //   val chatname:String,

    ){
        fun toChatItemUi(): ChatItemUI{
                val members = members.keys.toList()

                return ChatItemUI(
                        chatId = id,
                        type = type,
                        timestamp = timestamp,
                        moderators = moderators.keys.toList(),
                        members = members,
                        lastMessageId = lastMessage.toString(),
                        icon= null,
                        nameChat =""
                )
        }
}