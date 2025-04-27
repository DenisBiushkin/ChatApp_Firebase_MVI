package com.example.unmei.data.repository

import com.example.unmei.data.model.ChatRoomAdvance
import com.example.unmei.data.model.ChatRoomResponse
import com.example.unmei.domain.model.RoomsUser
import com.example.unmei.data.network.RemoteDataSource
import com.example.unmei.data.source.LocalDataSource
import com.example.unmei.domain.model.messages.ChatRoom
import com.example.unmei.domain.model.messages.Message
import com.example.unmei.domain.model.messages.NewRoomModel
import com.example.unmei.domain.model.messages.RoomSummaries
import com.example.unmei.domain.model.StatusUser
import com.example.unmei.domain.model.User
import com.example.unmei.domain.model.UserActivity
import com.example.unmei.domain.model.UserExtended
import com.example.unmei.domain.repository.MainRepository
import com.example.unmei.domain.util.ExtendedResource
import com.example.unmei.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import org.example.Mappers.base.Mapper
import javax.inject.Inject

class MainRepositoryImpl @Inject constructor(
    private val localDataSource: LocalDataSource, //  Room
    private val remoteDataSource: RemoteDataSource ,// Firebase,
    private val mapperChatRoom: Mapper<ChatRoomResponse, ChatRoom>
):MainRepository {
    override suspend fun getExistencePrivateGroupByUids(
        companionUid_1: String,
        companionUid_2: String
    ): String?{
        //дернуть из room
        return remoteDataSource.getExistenceChatByUids(companionUid_1,companionUid_2)
        //записать в room
    }

    override suspend fun sendMessageAdv(message: Message, chatId: String): Resource<Unit> {
       return remoteDataSource.sendMessageAdvRemote(message,chatId)
    }

    override suspend fun updateUnreadCountInRoomSummaries(
        roomId: String,
        newUnreadCount: Map<String, Int>
    ): Boolean {
        return remoteDataSource.updateUnreadCountInRoomSummariesRemote(roomId, newUnreadCount)
    }

    override suspend fun resetUnreadCountMessage(roomId: String, userId: String) {
        return remoteDataSource.resetUnreadCountMessageRemote(roomId,userId)
    }
    override suspend fun updateActiveUserInRoomRemote(
        roomId: String,
        userId: String,
        active: UserActivity
    ) {
        return remoteDataSource.updateActiveUserInRoomRemote(roomId,userId,active)
    }



    override suspend fun createNewChatAdvence(newRoomModel: NewRoomModel): Resource<String> {
        return remoteDataSource.createNewRoomAdvence(newRoomModel)
    }

    override suspend fun deleteChatAdvance(roomId: String): Resource<Unit> {
        return remoteDataSource.cascadeDeleteRoomsAdvence(roomId)
    }

    override fun getBlockMessagesByChatId(
        chatId: String,
        count: Int,
        lastMessageKey: String?
    ): Flow<Resource<List<Message>>> {
        //сначала вызываем из локалки
        return remoteDataSource.getBlockMessagesByChatIdRemote(chatId,count,lastMessageKey)
    }

    override fun deleteMessagesInChat(
        messagesId: List<String>,
        chatId: String
    ): Flow<Resource<Unit>> {
        return remoteDataSource.deleteMessagesInChat(messagesId,chatId)
    }

    override suspend fun setStatusUser(userId: String, status: StatusUser): Resource<String> {
       return remoteDataSource.setStatsuById(userId,status)
    }

    override suspend fun getUserById(userId: String): User? {
        return remoteDataSource.getUserByIdRemote(userId)
    }

    override fun observeMessagesInChat(chatId: String): Flow<ExtendedResource<Message>> {
        return remoteDataSource.observeMessagesInChat(chatId)
    }


///Users
    override fun observeStatusUserById(userId: String): Flow<StatusUser> {
        return remoteDataSource.observeStatusUserById(userId).mapNotNull {
            it.toStatusUser()
        }
    }

    override suspend fun getFriendsByUserId(userId: String): List<UserExtended>? {
        return remoteDataSource.getFriendsByUserIdRemote(userId)
    }

    override fun observeRoomSummaries(chatId: String): Flow<RoomSummaries> {
       return remoteDataSource.observeRoomSammaries(chatId)
    }

    override suspend fun isUserExist(userId:String):Boolean{
        return remoteDataSource.isUserExists(userId)
    }

    override suspend fun updateUsernameInProfile(
        userId: String,
        newUserName: String,
        oldUserName: String
    ): Boolean {
        return remoteDataSource.updateUsernameInProfileRemote(userId,newUserName,oldUserName)
    }

    override suspend fun updateFullNameInProfileRemote(
        userId: String,
        newFullName: String,
        oldFullName: String
    ): Boolean {
        return remoteDataSource.updateFullNameInProfileRemote(userId,newFullName,oldFullName)
    }

    override suspend fun getExistenceUsername(username: String): Boolean {
        return remoteDataSource.getExistenceUsername(username)
    }

    override suspend fun saveUser(user: User): Resource<Unit> {

        return  remoteDataSource.saveUserData(user=user)

    }

    override suspend fun getUsersWithStatus(userIds: List<String>): List<UserExtended>? {
        return remoteDataSource.getUsersWithStatusRemote(userIds)
    }

    override suspend fun getUsersIdsByFullName(fullName: String): List<String> {
        return remoteDataSource.getUsersIdsByFullName(fullName)
    }

    override suspend fun observeUser(userId: String): Flow <User> {
        return remoteDataSource.observeUser(userId)
    }

    override fun createNewChat(group: ChatRoom): Flow<Resource<String>> {
        return remoteDataSource.createNewChat(group)
    }


    //переделать чтобы цепляла сначала данные из локальной БД!


    override fun observeChatRoomAdvance(roomId:String): Flow<ChatRoomAdvance> {


       return remoteDataSource.observeChatRoom(roomId).map {
           it.toChatRoomAdvence()
       }


    }


    //перед запросом в сеть запрашивать из локального резитория!
    override fun observeRoomsUser(userId: String): Flow<RoomsUser>{
        return remoteDataSource.observeUserRooms(userId)
    }
}