package com.example.unmei.domain.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json


@Serializable
data class User(
    val uid:String="",
    val fullName:String,
    val userName:String,

    val phoneNumber:String?=null,
    val email:String?=null,
    val age:String?=null,
    val timestamp:Long = 0L,

    val photoUrl:String,


    val friends: List<String> = emptyList(),
    val rooms: List<String> =  emptyList(),
){
    fun toJson():String{
        return Json.encodeToString(this)
    }
    companion object{
        fun fromJson(jsonUser:String):User{
            return Json.decodeFromString(jsonUser)
        }
    }
}


