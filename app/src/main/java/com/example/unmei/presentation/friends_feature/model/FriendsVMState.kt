package com.example.unmei.presentation.friends_feature.model

data class FriendsVMState(

    val searchQuery:String ="",
    val isFocusedTextField:Boolean=false,

    val myFriends:List<FriendItemUi> = emptyList(),

    val isLoading:Boolean= false
)
