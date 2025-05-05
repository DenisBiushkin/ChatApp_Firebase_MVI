package com.example.unmei.presentation.profile_user_feature.model

data class ProfileVMState(
    val userId:String="",
    val fullName: String="",
    val iconUrl:String="",
    val statusUser:String= "Offline",
    val isMine:Boolean = false,
    val userName:String="",
    val age:String? = null,
    val timeStamp:Long= 0L,
    val friends:List<String> = emptyList(),
    val listInfo: List<BlockInfo> = emptyList(),
    val content:ProfileDetailScreenContent = ProfileDetailScreenContent.LOADING
)