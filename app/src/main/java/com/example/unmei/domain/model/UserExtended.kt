package com.example.unmei.domain.model

import com.example.unmei.presentation.create_group_feature.model.CreateGroupItemUi
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
    fun toGroupContacts():CreateGroupItemUi =this.run {
        CreateGroupItemUi(
            id= user.uid,
            fullName = user.fullName,
            lastSeen = statusUser.status.name,
            iconUrl = user.photoUrl
        )
    }
}
