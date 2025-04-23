package com.example.unmei.data.network

import android.content.Context
import android.net.Uri
import androidx.annotation.OptIn
import androidx.media3.common.util.Log
import androidx.media3.common.util.UnstableApi
import com.example.unmei.R
import com.example.unmei.data.model.ChatRoomAdvence
import com.example.unmei.data.model.ChatRoomResponse
import com.example.unmei.data.model.ChatRoomResponseAdvence
import com.example.unmei.data.model.MessageResponse
import com.example.unmei.data.model.RoomSummariesResp
import com.example.unmei.data.model.StatusUserResponse
import com.example.unmei.domain.model.ChatRoom
import com.example.unmei.domain.model.Message
import com.example.unmei.domain.model.NewRoomModel
import com.example.unmei.domain.model.RoomSummaries
import com.example.unmei.domain.model.RoomsUser
import com.example.unmei.domain.model.Status
import com.example.unmei.domain.model.StatusUser
import com.example.unmei.domain.model.TypeRoom
import com.example.unmei.domain.model.User
import com.example.unmei.domain.util.ExtendedResource
import com.example.unmei.util.ConstansApp.CHATS_BY_USERS_REFERENCE_DB
import com.example.unmei.util.ConstansApp.MESAGES_SUMMERIES_DB
import com.example.unmei.util.ConstansApp.MESSAGES_REFERENCE_DB
import com.example.unmei.util.ConstansApp.PRESENCE_USERS_REFERENCE_DB
import com.example.unmei.util.ConstansApp.ROOMS_REFERENCE_DB
import com.example.unmei.util.ConstansApp.ROOMS_REFERENCE_STORAGE
import com.example.unmei.util.ConstansApp.USERS_REFERENCE_DB
import com.example.unmei.util.ConstansDev
import com.example.unmei.util.ConstansDev.TAG
import com.example.unmei.util.Resource
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ServerValue
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue
import com.google.firebase.database.ktx.getValue
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow

import kotlinx.coroutines.tasks.await
import okio.Path


class RemoteDataSource(
    private val db:FirebaseDatabase,
    private val storage:FirebaseStorage,
    private val context: Context
) {
    private  val usersRef = db.getReference(USERS_REFERENCE_DB)
    private  val roomsRef = db.getReference(ROOMS_REFERENCE_DB)
    private  val messagesRef= db.getReference(MESSAGES_REFERENCE_DB)
    private  val presenceUsersRef= db.getReference(PRESENCE_USERS_REFERENCE_DB)
    private val chatsByUsers = db.getReference(CHATS_BY_USERS_REFERENCE_DB)
    private val reference = db.getReference()

    private  val messagesSummariesRef= db.getReference(MESAGES_SUMMERIES_DB)
    private val storageRoomsRef=storage.getReference("Rooms")


//100 working
    fun observeRoomSammaries(
        roomId:String
    ):Flow<RoomSummaries> = callbackFlow{
        val reference =messagesSummariesRef.child(roomId)
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val data = snapshot.getValue(RoomSummariesResp::class.java)
//                data?.let {
//                    trySend(it.toRoomSummaries())
//                }
                if(data!=null)
                    trySend(data.toRoomSummaries())
                else
                    trySend(RoomSummaries())
            }

            override fun onCancelled(error: DatabaseError) {
               //
            }
        }
        reference.addValueEventListener(listener)
        awaitClose { reference.removeEventListener(listener) }

    }

    suspend fun saveToStorageByPath(
        filePath: String,
        fileName:String,
        uriData: Uri
    ):Resource<Uri>{
        try {
            val fileRef=storage.reference.child("$filePath/$fileName")
            fileRef.putFile(uriData).await()
            val resultUri = fileRef.downloadUrl.await()
            return Resource.Success(resultUri)
        }catch (e:Exception){
            return Resource.Error(message = e.toString())
        }
    }

    suspend fun getChatRoomById(roomId:String): ChatRoomAdvence?{
        try{
            val result = roomsRef.child(roomId).get().await()
            if (result.exists()){
                return result.getValue(ChatRoomResponseAdvence::class.java)?.toChatRoomAdvence()
            }else
                return null
        }catch (e:Exception){
                return null
        }
    }

    //Unknown Working(working)
    //
    @OptIn(UnstableApi::class)
    suspend fun createNewRoomAdvence(
        newRoomModel: NewRoomModel
    ):Resource<String>{
        try {
            val key= roomsRef.push().key.toString()
            val keyMessage = messagesRef.push().key.toString()
            var roomIconUrl = newRoomModel.standartUrlIcon
            when(newRoomModel.type){
                TypeRoom.PRIVATE -> {
                    roomIconUrl = newRoomModel.iconUrl
                }
                TypeRoom.PUBLIC -> {
                    val result=saveToStorageByPath(
                        filePath = "$ROOMS_REFERENCE_STORAGE/$key",
                        fileName="$key.png",
                        uriData =newRoomModel. iconUri
                    )
                    if(result is Resource.Success){
                        roomIconUrl = result.data.toString()
                    }
                }
            }
            val room=ChatRoomResponseAdvence(
                id = key,
                chatName=newRoomModel.chatName,
                type = newRoomModel.type.value,
                iconUrl =  roomIconUrl,
                timestamp = ServerValue.TIMESTAMP,
                moderators = if (!newRoomModel.moderatorsIds.isEmpty()) newRoomModel.moderatorsIds.map { it to true }.toMap() else emptyMap(),
                members = newRoomModel.membersIds.map { it to true }.toMap(),
                activeUsers = emptyMap()
            )
            val mesgResp=newRoomModel.message?.let { MessageResponse().fromMessageToResp(it.copy(id = keyMessage))}
            val summaries = RoomSummariesResp(
                lastMessage =mesgResp,
                unreadedCount = newRoomModel.membersIds.map { it to 0 }.toMap()
            )

            val updates = mutableMapOf(
                "${ROOMS_REFERENCE_DB}/$key" to room,
                "${MESSAGES_REFERENCE_DB}/$key/$keyMessage" to mesgResp,
                "${MESAGES_SUMMERIES_DB}/$key" to summaries
            )
            newRoomModel.membersIds.forEach {
                //ДОБАВИТЬ UID чата каждому участнику
                updates["${USERS_REFERENCE_DB}/$it/rooms/$key"]=true
            }
            if(newRoomModel.type== TypeRoom.PRIVATE){
                val uniqueKey = chatsByUsersGenKey(members = newRoomModel.membersIds)
                Log.d(TAG,"Unique_Key: $uniqueKey")
                updates["${CHATS_BY_USERS_REFERENCE_DB}/$uniqueKey"] = key
            }
            reference.updateChildren(updates).await()
            return Resource.Success(data = key)
        }catch (e:Exception){
            return Resource.Error(message = e.toString())
        }
    }
    //рабочая 22.03
    suspend fun cascadeDeleteRoomsAdvence(
        roomId:String
    ):Resource<Unit>{
        try {
            //Удаление

            //summaries,group,messages
            val updates = mapOf(
                "${ROOMS_REFERENCE_DB}/$roomId" to null,
                "${MESSAGES_REFERENCE_DB}/$roomId" to null,
                "${MESAGES_SUMMERIES_DB}/$roomId" to null

                //Удалить UID чата у каждого участника
            )
            reference.updateChildren(updates).await()
            return Resource.Success(Unit)
        }catch (e:Exception){
            return Resource.Error(message = e.toString())
        }
    }




    suspend fun isUserExists(userId: String): Boolean {
        return try {
            val dataSnapshot = usersRef.child(userId).get().await()
            dataSnapshot.exists()
        } catch (e: Exception) {
            false // Обрабатываем ошибки (например, отсутствует подключение)
        }
    }
    @Deprecated(message = "Доработать добавление статуса online в presence")
    suspend fun saveUserData(user:User):Resource<Unit>{
        try {
            usersRef.child(user.uid).setValue(user).await()
            return Resource.Success(Unit)
        }catch (e:Exception){
            return Resource.Error(message = e.toString())
        }

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

    @Deprecated("Удалят и наши и ваши сообщения")
    fun deleteMessagesInChat(messagesId:List<String>,chatId: String):Flow<Resource<Unit>> = flow{
        val updates = mutableMapOf<String,Any?>()
        val reference = db.getReference()
        messagesId.forEach {
            val messagePath =  "${MESSAGES_REFERENCE_DB}/$chatId/$it"
            updates[messagePath] = null
        }
        try {
            emit(Resource.Loading())
            reference.updateChildren(updates).await()
            emit(Resource.Success(Unit))
        }catch (e:Exception){
            emit(Resource.Error(message = e.toString()))
        }
    }

    //good
    fun  observeMessagesInChat(roomId: String):Flow<ExtendedResource<Message>> = callbackFlow{
        var index = 0
        val listener = object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val newMessage = snapshot.getValue(MessageResponse::class.java)
                newMessage?.let {
                    trySend(ExtendedResource.Added(it.toMessage()))
                }
            }
            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                val newMessage = snapshot.getValue(MessageResponse::class.java)
                newMessage?.let {
                    trySend(ExtendedResource.Edited(it.toMessage()))
                }
            }
            override fun onChildRemoved(snapshot: DataSnapshot) {
                val newMessage = snapshot.getValue(MessageResponse::class.java)
                newMessage?.let {
                    trySend(ExtendedResource.Removed(it.toMessage()))
                }
            }
            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {

            }

            override fun onCancelled(error: DatabaseError) {
                trySend(ExtendedResource.Error(message = error.toString()))
            }
        }
        val ref= messagesRef.child(roomId).limitToLast(1)//только новые
        ref.addChildEventListener(listener)
        awaitClose{
            ref.removeEventListener(listener)
        }
    }


    fun getBlockMessagesByChatIdRemote(
        chatId:String,
        count:Int = 20,
        lastMessageKey: String?
    ): Flow<Resource<List<Message>>> = flow{
        val baseref=messagesRef.child(chatId)
        try{
            emit(Resource.Loading())

            var query =messagesRef.child(chatId).limitToLast(count)

            if( lastMessageKey!=null){
               query=messagesRef.child(chatId).limitToLast(count).orderByKey().endBefore(lastMessageKey)
            }

            val snapshot= query
                .get()
                .await()
            //сырые данные
            val newMessages = snapshot.children.mapNotNull {
                it.getValue(MessageResponse::class.java)
            }
            Log.d(TAG,newMessages.toString())
            emit(Resource.Success(data = newMessages.map { it.toMessage() }))
        }catch (e:Exception)
        {
            emit(Resource.Error(message = e.toString()))
        }
    }

    suspend fun getUserByIdRemote(userId: String):User?{
        val reference= usersRef.child(userId)
        try {
            val data=reference.get().await()
            return data.getValue(User::class.java)
        }catch (e:Exception){
            return null
        }
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
                    val chatByUserKey=chatsByUsersGenKey(members.keys.toList())
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


    //рабочая на 22.03
    suspend fun getExistenceChatByUids(companionUid_1:String,companionUid_2:String):String?{
          val uniqueKey = chatsByUsersGenKey(listOf(companionUid_1,companionUid_2))
          try {
              val data = chatsByUsers.child(uniqueKey).get().await()
              if (data.exists()){
                  val chatUId =data.getValue(String::class.java)
                  return chatUId

              }else{
                  return null
              }
          }catch (e:Exception){
              return null
          }
    }
    //свежая на 22.03
    suspend fun sendMessageAdvRemote(message: Message,chatId:String):Resource<Unit>{
        val uidMessage= messagesRef.push().key!!
        val msg= MessageResponse().fromMessageToResp(message).copy(
            id= uidMessage
        )
        val updates = mapOf(
            "${MESSAGES_REFERENCE_DB}/$chatId/$uidMessage" to msg,
            "$MESAGES_SUMMERIES_DB/$chatId/lastMessage" to msg
            //Удалить UID чата у каждого участника
        )
        try{
            reference.updateChildren(updates).await()
            return Resource.Success(Unit)
        }catch (e:Exception){
            return Resource.Error(message = e.toString())
        }
    }


    private fun chatsByUsersGenKey(members:List<String>):String{
        //генерирует уникальный ключ связывающий 2 пользователя
        val keys = members
        val sorted =keys.map { it.toCharArray().sorted().joinToString("") }
        return sorted.sorted().joinToString ("_")
    }

}