package com.example.unmei.presentation.registration_feature.model

data class RegistrationVMState(
    val fullName:String="",
    val emailOrPhone:String="",
    val firstPassword:String="",
    val secondPassword:String="",

    val isLoading:Boolean=false,
    val showAlert:Boolean=false,
    val textAlert:String=""
)