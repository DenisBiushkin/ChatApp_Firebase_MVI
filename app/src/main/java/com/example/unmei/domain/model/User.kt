package com.example.unmei.domain.model

data class User(
    val uid:String="",
    val fullName:String="",
    val userName:String="",
    val phoneNumber:String="",
    val email:String="",
    val age:String="",
    val photo:String="",
    val isOnline:Boolean = false,
    val friends: Map<String,Boolean> = emptyMap(),
    val rooms: Map<String,Boolean> = emptyMap(),
)
