package com.example.unmei.domain.repository

import com.example.unmei.domain.model.ChatRoom
import com.example.unmei.domain.model.Message
import com.example.unmei.domain.model.NewRoomModel
import com.example.unmei.domain.model.RoomsUser
import com.example.unmei.domain.model.StatusUser
import com.example.unmei.domain.model.User
import com.example.unmei.domain.util.ExtendedResource
import com.example.unmei.presentation.Navigation.Screens
import com.example.unmei.util.Resource
import kotlinx.coroutines.flow.Flow

interface MainRepository {

    fun saveUser(user: User): Flow<Resource<Boolean>>

    suspend fun isUserExist(userId:String):Boolean

    suspend fun  observeUser(userId: String):Flow<User>

    suspend fun getUserById(userId: String):User?

    fun observeStatusUserById(userId: String):Flow<StatusUser>

    suspend fun setStatusUser(userId:String,status:StatusUser):Resource<String>

    //MessagesRepository
    fun observeRoomsUser(userId:String): Flow<RoomsUser>
    fun observeChatRoom(roomId:String): Flow<ChatRoom>
    fun createNewChat(group :ChatRoom):Flow<Resource<String>>
    fun getBlockMessagesByChatId(chatId:String,count:Int = 20,lastMessageKey: String? = null): Flow<Resource<List<Message>>>

    fun observeMessagesInChat(chatId: String):Flow<ExtendedResource<Message>>

    fun deleteMessagesInChat(messagesId:List<String>,chatId: String):Flow<Resource<Unit>>



    suspend fun createNewChatAdvence( newRoomModel: NewRoomModel):Resource<String>

    suspend fun deleteChatAdvance(roomId:String):Resource<Unit>
}