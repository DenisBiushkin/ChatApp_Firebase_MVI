package com.example.unmei.presentation.registration_feature.model

sealed class RegistrationVMEvent{

    data class FullNameChange(
        val value:String
    ):RegistrationVMEvent()

    data class EmailOrPhoneChange(
        val value:String
    ):RegistrationVMEvent()

    data class FirstPasswordChange(
        val value:String
    ):RegistrationVMEvent()

    data class SecondPasswordChange(
        val value:String
    ):RegistrationVMEvent()

    object Registration:RegistrationVMEvent()

}
