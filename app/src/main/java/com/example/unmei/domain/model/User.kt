package com.example.unmei.domain.model

data class User(
    val uid:String="",
    val fullName:String,
    val userName:String,

    val phoneNumber:String?=null,
    val email:String?=null,
    val age:String?=null,

    val photoUrl:String,


    val friends: List<String> = emptyList(),
    val rooms: List<String> =  emptyList(),
)


