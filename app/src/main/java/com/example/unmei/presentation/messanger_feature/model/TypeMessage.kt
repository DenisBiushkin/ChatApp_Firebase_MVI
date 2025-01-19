package com.example.unmei.presentation.messanger_feature.model

sealed class TypeMessage {

    object Text : TypeMessage()
    object Photo : TypeMessage()
    object Audio : TypeMessage()
}