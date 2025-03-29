package com.example.unmei.presentation.profile_user_feature.model

data class ProfileVMState(
    val isLoading: Boolean = true,
    val userId:String="",
    val fullName: String="",
    val iconUrl:String="",
    val statusOnline:String= "Offline",
    val isMine:Boolean = false,
    val userName:String=""
)