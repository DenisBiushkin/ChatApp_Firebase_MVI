package com.example.unmei.domain.repository

import com.example.unmei.util.Resource

interface FriendRepository {


    fun getFriendById(userId:String)

    fun addFriendById(userId: String,friendId:String):Boolean
}

