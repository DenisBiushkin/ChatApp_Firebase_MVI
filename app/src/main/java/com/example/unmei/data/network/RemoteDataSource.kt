package com.example.unmei.data.network

import com.example.unmei.domain.model.User
import com.example.unmei.util.ConstansApp.USERS_REFERENCE_DB
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.tasks.await

class RemoteDataSource(
    private val db:FirebaseDatabase
) {
    private  val ref = db.getReference(USERS_REFERENCE_DB)

    suspend fun isUserExists(userId: String): Boolean {
        return try {
            val dataSnapshot = ref.child(userId).get().await()
            dataSnapshot.exists()
        } catch (e: Exception) {
            false // Обрабатываем ошибки (например, отсутствует подключение)
        }
    }
    suspend fun saveUserData(user:User){
        ref.child(user.uid).setValue(user).await()
    }


}