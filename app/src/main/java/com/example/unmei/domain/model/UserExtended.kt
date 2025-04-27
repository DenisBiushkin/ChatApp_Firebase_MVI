package com.example.unmei.domain.model

import com.example.unmei.presentation.friends_feature.model.FriendItemUi

data class UserExtended(
    val user: User,
    val statusUser: StatusUser
){
    fun toFriendItemUi():FriendItemUi = this.let {
        FriendItemUi(
            uid = it.user.uid,
            fullName = it.user.fullName,
            iconUrl = it.user.photoUrl,
            isOnline = it.statusUser.status == Status.ONLINE,
            isFriend =true
        )
    }
}
