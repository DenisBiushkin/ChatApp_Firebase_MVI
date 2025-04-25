package com.example.unmei.presentation.friends_feature.model

data class FriendItemUi(
    val uid:String,
    val fullName:String,
    val iconUrl:String,
    val isOnline:Boolean = false,

    val isFriend: Boolean = false
)
