package com.example.unmei.presentation.friends_feature.model

sealed class FriendVMEvent{


    data class SearchFieldChanged(
        val value:String
    ):FriendVMEvent()

}
