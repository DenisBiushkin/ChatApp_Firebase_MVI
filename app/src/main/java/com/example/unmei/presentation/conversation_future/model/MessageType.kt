package com.example.unmei.presentation.conversation_future.model

sealed class MessageType(){
    class Image(
        val mediaUrl: String
    ) : MessageType()
    object Text:MessageType()

}