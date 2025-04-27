package com.example.unmei.presentation.friends_feature.model

data class FriendsVMState(

    val searchQuery:String ="",
    val isSearchActive:Boolean=false,

    val myFriendsList: List<FriendItemUi> = emptyList(),
    val searchResultList:List<FriendItemUi> = emptyList(),

    val contentState: FriendsContentState=FriendsContentState.Loading
)
