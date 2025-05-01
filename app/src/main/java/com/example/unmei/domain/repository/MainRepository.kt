package com.example.unmei.domain.repository

import com.example.unmei.data.model.ChatRoomAdvance
import com.example.unmei.data.network.RemoteDataSource
import com.example.unmei.domain.model.AttachmentDraft
import com.example.unmei.domain.model.messages.ChatRoom
import com.example.unmei.domain.model.messages.Message
import com.example.unmei.domain.model.messages.NewRoomModel
import com.example.unmei.domain.model.messages.RoomSummaries
import com.example.unmei.domain.model.RoomsUser
import com.example.unmei.domain.model.StatusUser
import com.example.unmei.domain.model.TypingStatus
import com.example.unmei.domain.model.UploadProgress
import com.example.unmei.domain.model.User
import com.example.unmei.domain.model.UserActivity
import com.example.unmei.domain.model.UserExtended
import com.example.unmei.domain.util.ExtendedResource
import com.example.unmei.util.Resource
import kotlinx.coroutines.flow.Flow

interface MainRepository {

    suspend fun saveUser(user: User): Resource<Unit>

    suspend fun isUserExist(userId:String):Boolean

    suspend fun updateUsernameInProfile(userId:String, newUserName:String, oldUserName:String):Boolean
    suspend fun updateFullNameInProfileRemote(userId:String, newFullName:String, oldFullName:String):Boolean
    suspend fun getExistenceUsername(username:String):Boolean
    suspend fun getUsersIdsByFullName(fullName:String):List<String>

    suspend fun  observeUser(userId: String):Flow<User>

    suspend fun getUserById(userId: String):User?

    fun observeStatusUserById(userId: String):Flow<StatusUser>

    suspend fun setStatusUser(userId:String,status:StatusUser):Resource<String>

    suspend fun getUsersWithStatus(userIds: List<String>):List<UserExtended>?

    suspend fun getFriendsByUserId(userId: String):List<UserExtended>?


    //MessagesRepository
    fun uploadAttachmentWithProgressRemote(pathString: String, draft: AttachmentDraft):Flow<UploadProgress>

    fun observeRoomsUser(userId:String): Flow<RoomsUser>

    suspend fun updateStatusUserById(groupId:String,userId: String,status: TypingStatus)




    fun observeChatRoomAdvance(roomId:String): Flow<ChatRoomAdvance>


    fun createNewChat(group : ChatRoom):Flow<Resource<String>>
    fun getBlockMessagesByChatId(chatId:String,count:Int = 20,lastMessageKey: String? = null): Flow<Resource<List<Message>>>

    fun observeMessagesInChat(chatId: String):Flow<ExtendedResource<Message>>

    fun deleteMessagesInChat(messagesId:List<String>,chatId: String):Flow<Resource<Unit>>

    fun observeRoomSummaries(chatId: String):Flow<RoomSummaries>

    //свежее (почти сырое)
    suspend fun updateUnreadCountInRoomSummaries(roomId: String, newUnreadCount:Map<String,Int>):Boolean
    suspend fun resetUnreadCountMessage(roomId: String, userId: String)
    suspend fun updateActiveUserInRoomRemote(roomId: String, userId: String, active: UserActivity)

    suspend fun createNewChatAdvence( newRoomModel: NewRoomModel):Resource<String>
    suspend fun deleteChatAdvance(roomId:String):Resource<Unit>
    suspend fun  getExistencePrivateGroupByUids(companionUid_1:String,companionUid_2:String):String?

    suspend fun sendMessageAdv(message: Message, chatId:String):Resource<Unit>
}