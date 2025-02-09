package com.example.unmei.data.network

import androidx.annotation.OptIn
import androidx.media3.common.util.Log
import androidx.media3.common.util.UnstableApi
import com.example.unmei.domain.model.ChatRoom
import com.example.unmei.domain.model.Message
import com.example.unmei.domain.model.RoomsUser
import com.example.unmei.domain.model.User
import com.example.unmei.util.ConstansApp.ROOMS_REFERENCE_DB
import com.example.unmei.util.ConstansApp.USERS_REFERENCE_DB
import com.example.unmei.util.ConstansDev.TAG
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class RemoteDataSource(
    private val db:FirebaseDatabase
) {
    private  val usersRef = db.getReference(USERS_REFERENCE_DB)
    private  val roomsRef = db.getReference(ROOMS_REFERENCE_DB)

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
                trySend(RoomsUser(roomsMap))

            }
            override fun onCancelled(error: DatabaseError) {
                //
            }
        }
        reference.addValueEventListener(listener)
        awaitClose {reference.removeEventListener(listener) }
    }

    fun observeChatRoom(roomId:String):Flow<ChatRoom> = callbackFlow {

        val reference = roomsRef.child(roomId)
        val listener= object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                trySend(snapshot.getValue(ChatRoom::class.java)?: ChatRoom())
            }

            override fun onCancelled(error: DatabaseError) {
                //
            }
        }
        reference.addValueEventListener(listener)
        awaitClose{ reference.removeEventListener(listener)}
    }


}