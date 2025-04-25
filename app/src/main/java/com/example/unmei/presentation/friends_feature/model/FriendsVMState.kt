package com.example.unmei.presentation.friends_feature.model

data class FriendsVMState(
    val myFriends:List<FriendItemUi> = emptyList(),

    val isLoading:Boolean= false
)
