package com.example.unmei.presentation.singleChat_feature.model

sealed class MessageType(){

    object OnlyImage: MessageType()

    object Text:MessageType()

}