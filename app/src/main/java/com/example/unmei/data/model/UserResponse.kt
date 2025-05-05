package com.example.unmei.data.model

import com.example.unmei.domain.model.User
import com.google.firebase.database.ServerValue

data class UserResponse(
    val uid: String="",
    val fullName: String="",
    val userName: String="",
    val photo:String="",
    val phoneNumber:String?= null,
    val email:String?= null,
    val age:String?= null,
    val timestamp: Any? =null,
    val friends: Map<String,Boolean> = emptyMap(),
    val rooms: Map<String,Boolean> = emptyMap(),
){
    companion object{
        fun toUserResponse(user: User):UserResponse{

            return user.run {
                UserResponse(
                    uid=uid,
                    fullName=fullName,
                    userName = userName,
                    photo=photoUrl,
                    friends = friends.map { it to true }.toMap(),
                    rooms = rooms.map { it to true }.toMap(),
                    timestamp = ServerValue.TIMESTAMP
                )
            }
        }
    }
    fun toUser()= this.run {
        User(
            uid =uid,
            fullName=fullName,
            userName = userName,
            photoUrl = photo,
            friends = friends.keys.toList(),
            rooms = rooms.keys.toList(),
            //nullable
            age = age,
            timestamp = when(timestamp){
                is Long -> timestamp
                else -> 0L
            },

        )
    }
}