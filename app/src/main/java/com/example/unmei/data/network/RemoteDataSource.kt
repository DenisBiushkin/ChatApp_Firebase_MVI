package com.example.unmei.data.network

import androidx.annotation.OptIn
import androidx.media3.common.util.Log
import androidx.media3.common.util.UnstableApi
import com.example.unmei.data.model.ChatRoomResponse
import com.example.unmei.data.model.MessageResponse
import com.example.unmei.data.model.StatusUserResponse
import com.example.unmei.domain.model.ChatRoom
import com.example.unmei.domain.model.Message
import com.example.unmei.domain.model.RoomsUser
import com.example.unmei.domain.model.Status
import com.example.unmei.domain.model.StatusUser
import com.example.unmei.domain.model.User
import com.example.unmei.presentation.chat_list_feature.model.MessageStatus
import com.example.unmei.presentation.conversation_future.model.MessageListItemUI
import com.example.unmei.presentation.conversation_future.model.MessageType
import com.example.unmei.util.ConstansApp
import com.example.unmei.util.ConstansApp.CHATS_BY_USERS_REFERENCE_DB
import com.example.unmei.util.ConstansApp.MESSAGES_REFERENCE_DB
import com.example.unmei.util.ConstansApp.PRESENCE_USERS_REFERENCE_DB
import com.example.unmei.util.ConstansApp.ROOMS_REFERENCE_DB
import com.example.unmei.util.ConstansApp.USERS_REFERENCE_DB
import com.example.unmei.util.ConstansDev
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
import kotlinx.coroutines.flow.catch
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


    @Deprecated(message = "Доработать добавление статуса online в presence")
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
    fun initFirstMassagesRemote(chatId:String): Flow<Resource<List<Message>>> = flow{

        try{
            emit(Resource.Loading())
            val snapshot=messagesRef.child(chatId)
                // .orderByChild("timestamp")
                .limitToLast(30)
                .get()
                .await()
            //сырые данные
            val newMessages = snapshot.children.mapNotNull {
                it.getValue(Message::class.java) }
            emit(Resource.Success(data = newMessages))
        }catch (e:Exception){
            emit(Resource.Error(message = e.toString()))
        }

    }.catch {
        emit(Resource.Error(message = "errro"))
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


    @Deprecated(message = "Не сипользовать сделал по другому")
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


   //work
    private suspend fun createNewRoom(room: ChatRoom):Resource<String>{
        val key= roomsRef.push().key.toString()
        val reference = db.getReference()
        try {
            val data= room.run {
                ChatRoomResponse(
                    timestamp = ServerValue.TIMESTAMP,
                    id = key,
                    moderators = moderators,
                    members = members,
                    type = type,
                    lastMessage = lastMessage
                )
            }
            val updates = mapOf(
                "${ROOMS_REFERENCE_DB}/$key" to data,
                "${MESSAGES_REFERENCE_DB}/$key" to null,
            )
            reference.updateChildren(updates).await()
            return Resource.Success(data = key)
        }catch (e:Exception){
            return  Resource.Error(message=e.toString())
        }
    }

    //рабочая
    suspend fun cascadingRoomDeleteWithMessage(roomId:String):Resource<Unit>{

        val reference = db.getReference()
        try{
            val updates = mapOf(
                "${ROOMS_REFERENCE_DB}/$roomId" to null,
                "${MESSAGES_REFERENCE_DB}/$roomId" to null,
            )
            reference.updateChildren(updates).await()
            return Resource.Success(data = Unit)
        }catch (e:Exception){
            return Resource.Error(message = e.toString())
        }
    }

    //рабочая
    suspend fun createPrivateRoom(users:List<String>,message: Message):Resource<String>{
        val reference = db.getReference()
        try {
            val members = mutableMapOf<String,Boolean>()
            users.forEach {
                members[it] = true
            }
            val room = ChatRoom(
                id="", timestamp = 0L,
                members =members,
                type = "private"
            )
            val requestData = createNewRoom(room)
            when(requestData){
                is Resource.Error ->{return Resource.Error(message = requestData.message!!) }
                is Resource.Loading -> {return Resource.Error(message = "OtherError")}
                is Resource.Success -> {
                    val roomId = requestData.data
                    val messageId=messagesRef.push().key
                    val chatByUserKey=chatsByUsersGenKey(members)
                    val updates = mapOf(
                        "${MESSAGES_REFERENCE_DB}/$roomId/$messageId" to message,
                        "${CHATS_BY_USERS_REFERENCE_DB}/$chatByUserKey" to roomId
                    )
                    reference.updateChildren(updates).await()
                    return Resource.Success(data = roomId)
                }
            }
        }catch (e:Exception){
            return Resource.Error(message = e.toString())
        }
    }
//рабочая
    suspend fun saveIdRoomInUsers(users:List<String>,roomId:String):Resource<Unit>{
        val reference = db.getReference()
        try {
            val updates= mutableMapOf<String,Any>()
            users.forEach {
                updates["${USERS_REFERENCE_DB}/$it/rooms/$roomId"] = true
            }
            reference.updateChildren(updates).await()
            return Resource.Success(data = Unit)
        }catch (e:Exception){
            return Resource.Error(message = e.toString())
        }
    }
    fun observeMessageInChat(roomId: String):Flow<List<Message>> = callbackFlow {

        val reference = messagesRef.child(roomId).orderByChild("timestamp").limitToLast(100)
        val listener=object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val listMessage= mutableListOf<MessageResponse>()
                snapshot.children.forEach {
                 //   println(it.value)
                    it.getValue(MessageResponse::class.java)?.let { it1 -> listMessage.add(it1) }
                }
                trySend(listMessage.map { it.toMessage() })
               snapshot.getValue(MessageResponse::class.java)
            }

            override fun onCancelled(error: DatabaseError) {
                //
            }
        }
        reference.addValueEventListener(listener)
        awaitClose{
            reference.removeEventListener(listener)
        }
    }


    private fun chatsByUsersGenKey(members:Map<String,Boolean>):String{
        val keys = members.keys.toList()
        val sorted =keys.map { it.toCharArray().sorted().joinToString("") }
        return sorted.sorted().joinToString ("_")
    }

}