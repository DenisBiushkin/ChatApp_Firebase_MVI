package com.example.unmei.presentation.friends_feature.model

sealed class FriendsContentState {

    data class Error(
        val Message:String
    ):FriendsContentState()

    object Loading:FriendsContentState()

    object Content:FriendsContentState()
}