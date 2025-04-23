package com.example.unmei.presentation.sign_in_feature.model

sealed class SignInVMEvent {

    data class EmailValueChange(
        val value:String
    ):SignInVMEvent()

    data class PasswordValueChange(
        val value:String
    ):SignInVMEvent()

    object SignInWithEmail:SignInVMEvent()
}