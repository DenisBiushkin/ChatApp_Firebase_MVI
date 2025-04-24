package com.example.unmei.data.repository

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
import com.example.unmei.domain.repository.MainRepository
import com.example.unmei.domain.util.ExtendedResource
import com.example.unmei.util.Resource
import kotlinx.coroutines.flow.Flow
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

    override fun observeRoomSummaries(chatId: String): Flow<RoomSummaries> {
       return remoteDataSource.observeRoomSammaries(chatId)
    }

    override suspend fun isUserExist(userId:String):Boolean{
        return remoteDataSource.isUserExists(userId)
    }

    override suspend fun saveUser(user: User): Resource<Unit> {

        return  remoteDataSource.saveUserData(user=user)

    }

    override suspend fun observeUser(userId: String): Flow <User> {
        return remoteDataSource.observeUser(userId)
    }

    override fun createNewChat(group: ChatRoom): Flow<Resource<String>> {
        return remoteDataSource.createNewChat(group)
    }


    //переделать чтобы цепляла сначала данные из локальной БД!
    override fun observeChatRoom(roomId:String): Flow<ChatRoom> {
       return remoteDataSource.observeChatRoom(roomId).mapNotNull {
           mapperChatRoom.map(it)
       }
    }


    //перед запросом в сеть запрашивать из локального резитория!
    override fun observeRoomsUser(userId: String): Flow<RoomsUser>{
        return remoteDataSource.observeUserRooms(userId)
    }
}