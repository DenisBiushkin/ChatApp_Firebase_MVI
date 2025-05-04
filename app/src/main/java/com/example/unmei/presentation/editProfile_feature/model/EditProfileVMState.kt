package com.example.unmei.presentation.editProfile_feature.model

data class EditProfileVMState(
    val userId:String = "",
    val firstName:String="",
    val secondName:String="",
    val userName:String = "",
    val iconUrl:String = "",
    val contentState: EditProfileScreenContent = EditProfileScreenContent.LOADING
)

enum class EditProfileScreenContent(
    val value: String? = null
){
    LOADING,
    CONTENT,
    ERROR
}
