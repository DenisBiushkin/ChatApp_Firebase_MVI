package com.example.unmei.presentation.singleChat_feature.model

sealed class MessageType(){

    class Image(
        val mediaUrl: String
    ) : MessageType()

    object Text:MessageType()

}