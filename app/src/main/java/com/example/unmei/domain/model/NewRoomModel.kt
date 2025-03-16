package com.example.unmei.domain.model

import android.net.Uri

data class NewRoomModel(
       val chatName: String,
       val type:TypeRoom,
       val iconUrl:String = "",
       val iconUri: Uri = Uri.EMPTY,
       val membersIds: List<String>,
       val moderatorsIds:List<String> = emptyList(),
       val standartUrlIcon:String = ""
    )