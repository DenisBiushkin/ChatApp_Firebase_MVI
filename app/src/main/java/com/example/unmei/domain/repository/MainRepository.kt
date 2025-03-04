package com.example.unmei.domain.repository

import com.example.unmei.domain.model.ChatRoom
import com.example.unmei.domain.model.Message
import com.example.unmei.domain.model.RoomsUser
import com.example.unmei.domain.model.StatusUser
import com.example.unmei.domain.model.User
import com.example.unmei.presentation.Navigation.Screens
import com.example.unmei.util.Resource
import kotlinx.coroutines.flow.Flow

interface MainRepository {


    fun saveUser(user: User): Flow<Resource<Boolean>>

    suspend fun isUserExist(userId:String):Boolean

    fun observeChatRoom(roomId:String): Flow<ChatRoom>

    suspend fun  observeUser(userId: String):Flow<User>

    fun observeRoomsUser(userId:String): Flow<RoomsUser>

    fun createNewChat(group :ChatRoom):Flow<Resource<String>>

    suspend fun getUserById(userId: String):User?

    fun observeStatusUserById(userId: String):Flow<StatusUser>

    suspend fun setStatusUser(userId:String,status:StatusUser):Resource<String>

     fun initFirstMassages(chatId:String): Flow<Resource<List<Message>>>
}