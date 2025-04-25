package com.example.unmei.domain.repository

import com.example.unmei.domain.model.UserExtended
import com.example.unmei.util.Resource

interface FriendRepository {


    suspend fun getFriendById(userId:String):UserExtended?

    suspend fun addFriendById(userId: String,friendId:String):Boolean

    suspend fun deleteFriendById(userId: String,friendId:String):Boolean
}

