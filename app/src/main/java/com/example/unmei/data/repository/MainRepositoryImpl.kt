package com.example.unmei.data.repository

import com.example.unmei.data.model.ChatRoomResponse
import com.example.unmei.domain.model.RoomsUser
import com.example.unmei.data.network.RemoteDataSource
import com.example.unmei.data.source.LocalDataSource
import com.example.unmei.domain.model.ChatRoom
import com.example.unmei.domain.model.StatusUser
import com.example.unmei.domain.model.User
import com.example.unmei.domain.repository.MainRepository
import com.example.unmei.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.mapNotNull
import org.example.Mappers.base.Mapper
import javax.inject.Inject

class MainRepositoryImpl @Inject constructor(
   private val localDataSource: LocalDataSource, //  Room
    private val remoteDataSource: RemoteDataSource ,// Firebase,
    private val mapperChatRoom: Mapper<ChatRoomResponse, ChatRoom>
):MainRepository {


    override suspend fun setStatusUser(userId: String, status: StatusUser): Resource<String> {
       return remoteDataSource.setStatsuById(userId,status)
    }

    override suspend fun getUserById(userId: String): User? {
        return null
    }

    override fun observeStatusUserById(userId: String): Flow<StatusUser> {
        return remoteDataSource.observeStatusUserById(userId).mapNotNull {
            it.toStatusUser()
        }
    }

    override suspend fun isUserExist(userId:String):Boolean{
        return remoteDataSource.isUserExists(userId)
    }

    override fun saveUser(user: User): Flow<Resource<Boolean>> {
        return flow {
            try{
                emit(Resource.Loading())

                remoteDataSource.saveUserData(user=user)

                emit(Resource.Success(data = true))
            }catch(e:Exception){
                emit(Resource.Error(message = e.toString()))
            }
        }
    }

    override suspend fun observeUser(userId: String): Flow <User> {
        return remoteDataSource.observeUser(userId)
    }

    //
    fun getUserRooms():Flow<List<RoomsUser>> = callbackFlow {

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