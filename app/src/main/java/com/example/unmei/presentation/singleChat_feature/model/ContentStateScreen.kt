package com.example.unmei.presentation.singleChat_feature.model



sealed class ContentStateScreen {

    object Loading :ContentStateScreen()

    object EmptyType :ContentStateScreen()

    object Content :ContentStateScreen()
}