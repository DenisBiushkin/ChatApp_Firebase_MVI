package com.example.unmei.presentation.chat_list_feature.model



data class ChatItemUI(
                 val chatId:String,
                 val members: List<String>,
                 val moderators:List<String>? ,
                 val timestamp:Long,
                 val type:String,
                 val lastMessageId:String,
                 val nameChat:String,
                 val isOnline:Boolean = false,
                 val typing:Boolean= false,
                 val icon:String?
)