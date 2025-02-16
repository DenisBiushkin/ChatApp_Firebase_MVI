package com.example.unmei.presentation.chat_list_feature.model

sealed class TypeMessage {

    object Text : TypeMessage()
    object Photo : TypeMessage()
    object Audio : TypeMessage()
}