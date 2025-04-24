package com.example.unmei.presentation.conversation_future.model



sealed class ContentStateScreen {

    object Loading :ContentStateScreen()

    object EmptyType :ContentStateScreen()

    object Content :ContentStateScreen()
}