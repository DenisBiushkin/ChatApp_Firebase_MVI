package com.example.unmei.presentation.conversation_future.model

sealed class ConversationEvent {

    object LoadingNewMessage :ConversationEvent()
}