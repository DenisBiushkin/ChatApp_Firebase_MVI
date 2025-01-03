package com.example.unmei.presentation.sign_in_feature.model

data class SignInResult (
    val data: UserData?,
    val errorMessage:String?,
)

data class UserData(
    val userId:String,
    val userName:String?,
    val ProfilePictureUrl:String?,
)