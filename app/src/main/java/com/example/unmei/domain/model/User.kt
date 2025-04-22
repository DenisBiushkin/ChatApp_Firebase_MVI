package com.example.unmei.domain.model

data class User(
    val uid:String="",
    val fullName:String="",
    val userName:String="",
    val phoneNumber:String="",
    val email:String="",
    val age:String="",
    val photo:String="",


    val online:Boolean = false,


    val friends: Map<String,Boolean> = emptyMap(),
    val rooms: Map<String,Boolean> = emptyMap(),
)

data class UserResponse(
    val uid: String,
    val fullName: String,
    val userName: String,
    val photoUrl:String,

    val phoneNumber:String?= null,
    val email:String?= null,
    val age:String?= null,

    val friends: Map<String,Boolean> = emptyMap(),
    val rooms: Map<String,Boolean> = emptyMap(),
)
