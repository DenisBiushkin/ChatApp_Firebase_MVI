package com.example.unmei.presentation.sign_in_feature.model

data class SignInVMState(
    val isSignInSuccess:Boolean=false,
    val signInErrorMessage:String?=null,

    val isLoading:Boolean=false,
    val showAlert:Boolean=false,
    val textAlert:String="",
    val emailField:String="",
    val passwordField:String=""
)
