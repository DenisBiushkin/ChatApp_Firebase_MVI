package com.example.unmei.presentation.friends_feature.model

sealed class FriendVMEvent{


    data class SearchFieldChanged(
        val value:String
    ):FriendVMEvent()

    data class SearchActiveChanged(
        val value:Boolean
    ):FriendVMEvent()

    data class AddNewFriend(
        val value: String
    ):FriendVMEvent()

}
