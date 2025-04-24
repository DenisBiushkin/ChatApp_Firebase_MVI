package com.example.unmei.domain.model.messages

import android.net.Uri
import com.example.unmei.domain.model.TypeRoom

data class NewRoomModel(
    val chatName: String,
    val type: TypeRoom,
    val iconUrl:String = "",
    val iconUri: Uri = Uri.EMPTY,
    val membersIds: List<String>,
    val moderatorsIds:List<String> = emptyList(),
    val standartUrlIcon:String = "",
    val message: Message? = null
    )