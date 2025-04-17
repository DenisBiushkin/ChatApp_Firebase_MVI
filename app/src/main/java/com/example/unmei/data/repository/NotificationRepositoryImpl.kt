package com.example.unmei.data.repository

import android.content.Context
import android.nfc.Tag
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.unmei.data.model.FcmData
import com.example.unmei.data.model.FcmMessage
import com.example.unmei.data.model.Notification
import com.example.unmei.data.model.NtfMessage
import com.example.unmei.data.network.retrofit.FcmApi
import com.example.unmei.domain.model.Message
import com.example.unmei.domain.model.RoomDetail
import com.example.unmei.domain.model.TypeRoom
import com.example.unmei.domain.repository.NotificationRepository
import com.example.unmei.util.ConstansApp.FCM_TOKEN_GET_URL
import com.example.unmei.util.ConstansApp.NOTIFICATION_TOKENS_DB
import com.example.unmei.util.ConstansApp.USERS_REFERENCE_DB
import com.example.unmei.util.ConstansDev.TAG
import com.example.unmei.util.Resource
import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.io.InputStream
import java.util.Collections
import javax.inject.Inject


//Таким способом отправлять Push уведомление КАТЕГОРИЧЕСКИ НЕЛЬЗЯ
//токен AUTH 2 должен получаться на стороннем сервере и отправка должна идти через него
//но за не имением такого сервака делаю так
class NotificationRepositoryImpl @Inject constructor(
    private val fcmApi: FcmApi,
    private val db: FirebaseDatabase,
    private val context: Context
) :NotificationRepository{

    private  val tokensByUsers = db.getReference(NOTIFICATION_TOKENS_DB)

    private lateinit var roomDetail: RoomDetail
    private lateinit var message: Message
    private lateinit var  notificationRecipientsId:List<String>
    private  var toTokenList = mutableListOf<String>()

    override suspend fun notifySendMessageInRooms(
        roomDetail: RoomDetail,
        message: Message,
        notificationRecipientsId: List<String>
    ) {
        this.roomDetail = roomDetail
        this.notificationRecipientsId=notificationRecipientsId
        this.message = message
        prepereBeforeSend()
        sendPushNotificationByToTokens()
    }

    private suspend fun prepereBeforeSend(){
        //запросить все токены
        toTokenList.clear()//т.к синглтон надо очистить это поле
        Log.d(TAG,"notificationRecipientsId: $notificationRecipientsId")
        notificationRecipientsId.forEachIndexed {index,it->
            Log.d(TAG,"Index $index")
            val userFcmToken=getFcmUserTokenByUserId(it) ?: return@forEachIndexed
            toTokenList.add(userFcmToken)
        }
    }

    private suspend fun sendPushNotificationByToTokens(){
        if (toTokenList.isEmpty()) return
        toTokenList.forEach {
            Log.d(TAG,"Sending Notification (NotificationRepositoryImpl) token:$it")
            fcmApi.sendMessage(
                body = FcmMessage(
                    message = NtfMessage(
                        token= it,
                        data = FcmData(
                            roomId = roomDetail.roomId,
                            typeRoom = roomDetail.typeRoom.value,
                            bodyType = "text",
                            title = roomDetail.roomName,
                            body =message.text?:"Empty text",
                            image = roomDetail.roomIconUrl
                        )
                    )
                )

            )
        }

    }

    override suspend fun saveNotificationTokenByUserId(token:String,userid: String):Resource<Unit>{
        val reference = db.getReference()
        try {
            reference.updateChildren(mapOf("${NOTIFICATION_TOKENS_DB}/$userid" to token)).await()
            return Resource.Success(data = Unit)
        }catch (e:Exception){
            return Resource.Error(message = e.toString())
        }
    }

    private suspend fun getFcmUserTokenByUserId(userid:String):String?{
        try {
            val snapshot=tokensByUsers.child(userid).get().await()

            return snapshot.getValue(String::class.java)

        }catch (e:Exception){
            return null
        }
    }
}