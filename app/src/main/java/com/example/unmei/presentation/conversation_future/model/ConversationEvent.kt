package com.example.unmei.presentation.conversation_future.model

sealed class ConversationEvent {

    object LoadingNewMessage :ConversationEvent()
    data class ChangeSelectedMessages (val id:String) :ConversationEvent()

    object OpenCloseBottomSheet :ConversationEvent()
  //  object CloseBottomSheet:ConversationEvent()
}