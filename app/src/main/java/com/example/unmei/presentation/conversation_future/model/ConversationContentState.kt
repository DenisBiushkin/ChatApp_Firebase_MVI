package com.example.unmei.presentation.conversation_future.model



sealed class ConversationContentState {


    object Loading :ConversationContentState()

    object EmptyType :ConversationContentState()

    object Messaging :ConversationContentState()
}