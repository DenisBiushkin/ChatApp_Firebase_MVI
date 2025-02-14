package com.example.unmei.data.network

import androidx.annotation.OptIn
import androidx.media3.common.util.Log
import androidx.media3.common.util.UnstableApi
import com.example.unmei.data.model.ChatRoomResponse
import com.example.unmei.data.model.StatusUserResponse
import com.example.unmei.domain.model.ChatRoom
import com.example.unmei.domain.model.Message
import com.example.unmei.domain.model.RoomsUser
import com.example.unmei.domain.model.Status
import com.example.unmei.domain.model.StatusUser
import com.example.unmei.domain.model.User
import com.example.unmei.util.ConstansApp.MESSAGES_REFERENCE_DB
import com.example.unmei.util.ConstansApp.PRESENCE_USERS_REFERENCE_DB
import com.example.unmei.util.ConstansApp.ROOMS_REFERENCE_DB
import com.example.unmei.util.ConstansApp.USERS_REFERENCE_DB
import com.example.unmei.util.ConstansDev.TAG
import com.example.unmei.util.Resource
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ServerValue
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

class RemoteDataSource(
    private val db:FirebaseDatabase
) {
    private  val usersRef = db.getReference(USERS_REFERENCE_DB)
    private  val roomsRef = db.getReference(ROOMS_REFERENCE_DB)
    private  val messagesRef= db.getReference(MESSAGES_REFERENCE_DB)
    private  val presenceUsersRef= db.getReference(PRESENCE_USERS_REFERENCE_DB)

    suspend fun isUserExists(userId: String): Boolean {
        return try {
            val dataSnapshot = usersRef.child(userId).get().await()
            dataSnapshot.exists()
        } catch (e: Exception) {
            false // Обрабатываем ошибки (например, отсутствует подключение)
        }
    }

    suspend fun saveUserData(user:User){
        usersRef.child(user.uid).setValue(user).await()
    }

    fun observeUser(userId: String): Flow<User> = callbackFlow {
        val reference= usersRef.child(userId)
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
               trySend(snapshot.getValue(User::class.java) ?: User())
            }
            override fun onCancelled(error: DatabaseError) {
              //
            }
        }
        reference.addValueEventListener(listener)
        awaitClose {reference.removeEventListener(listener) }
    }

    fun observeUserRooms(userId: String):Flow<RoomsUser> = callbackFlow {
        val reference= usersRef.child(userId).child("rooms")
        val listener = object : ValueEventListener {
            @OptIn(UnstableApi::class)
            override fun onDataChange(snapshot: DataSnapshot) {


                val roomsMap = snapshot.value as? Map<String, Boolean> ?: emptyMap()
                val roomsUser = RoomsUser(roomsMap)
                Log.d(TAG,"UserRoms DATA: "+roomsUser.rooms)
                trySend( roomsUser )

            }
            override fun onCancelled(error: DatabaseError) {
                //
            }
        }
        reference.addValueEventListener(listener)
        awaitClose {reference.removeEventListener(listener) }
    }

    fun observeChatRoom(roomId:String):Flow<ChatRoomResponse> = callbackFlow {

        val reference = roomsRef.child(roomId)
        val listener= object : ValueEventListener {
            @OptIn(UnstableApi::class)
            override fun onDataChange(snapshot: DataSnapshot) {
                val data= snapshot.getValue(ChatRoomResponse::class.java)?: ChatRoomResponse()
                Log.d(TAG, "ChatRoom Data: " + data.id)
                trySend(data)
            }

            override fun onCancelled(error: DatabaseError) {
                //
            }
        }
        reference.addValueEventListener(listener)
        awaitClose{ reference.removeEventListener(listener)}
    }

    fun createNewChat( group:ChatRoom):Flow<Resource<String>> = flow{
        val roomId = roomsRef.push().key.toString()
        try{
            emit(Resource.Loading())
            val room = group.run {
                ChatRoomResponse(
                    id = id,
                    timestamp = ServerValue.TIMESTAMP
                )
            }
            roomsRef.child(roomId).setValue(room)
            messagesRef.child(roomId)
            emit(Resource.Success(data = roomId))
        }catch (e:Exception){
            emit(Resource.Error(message = e.toString()))
        }

    }

    fun getUserById(userId: String): Flow<Resource<User>>  = flow {

    }
    fun observeStatusUserById(userId: String):Flow<StatusUserResponse> = callbackFlow {

        val reference = presenceUsersRef.child(userId)
        val listener = object :  ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val data = snapshot.getValue(StatusUserResponse::class.java)?: StatusUserResponse()
                trySend(data)
            }
            override fun onCancelled(error: DatabaseError) {
            }
        }
        reference.addValueEventListener(listener)
        awaitClose { reference.removeEventListener(listener) }
    }

   suspend fun setStatsuById(userId:String,status:StatusUser):Resource<String>{
       //написать потом mapper
       val stat = when(status.status){
           Status.OFFLINE -> "offline"
           Status.ONLINE -> "online"
           Status.RECENTLY -> "recently"
       }
       try {
           presenceUsersRef.child(userId).setValue(StatusUserResponse(
               status = stat,
               lastSeen = status.lastSeen
           )).await()
           return Resource.Success(data = "")

       }catch (e:Exception){
           return Resource.Error(message = e.toString())
       }
   }

    suspend fun createNewRoom(room: ChatRoom,message: Message):String{
        val key= roomsRef.push().key.toString()
        try {
            roomsRef.child(key).setValue(
                room.run {
                    ChatRoomResponse(
                        timestamp = ServerValue.TIMESTAMP,
                        id = key,
                        moderators = moderators,
                        members = members,
                        type = type,
                        lastMessage = lastMessage
                    )
                }
            ).await()
            messagesRef.child(key).setValue(message).await()
        }catch (e:Exception){

        }
        return key
    }



}