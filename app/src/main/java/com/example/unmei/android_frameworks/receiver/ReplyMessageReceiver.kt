package com.example.unmei.android_frameworks.receiver


import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.RemoteInput
import com.example.unmei.DI.ReceiverEntryPoint

import com.example.unmei.android_frameworks.receiver.model.NotificationDataExtra
import com.example.unmei.domain.model.messages.Message
import com.example.unmei.util.ConstansApp.KEY_TEXT_REPLY
import com.example.unmei.util.ConstansApp.NOTIFICATION_ID_PENDING_EXTRAS
import com.example.unmei.util.Resource
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.EntryPointAccessors
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


class ReplyMessageReceiver @Inject constructor(
   // private val sendMessageUseCase: NotifySendMessageUseCase
):BroadcastReceiver() {


    override fun onReceive(context: Context?, intent: Intent?) {

        val remoteInput = intent?.let { RemoteInput.getResultsFromIntent(it) } ?: return
        val safeContext = context ?: return
        val json = intent.getStringExtra(NOTIFICATION_ID_PENDING_EXTRAS) ?: return

        val extrasData = NotificationDataExtra.fromJson(json)
        val textMessage=remoteInput.getCharSequence(KEY_TEXT_REPLY).toString()

        val entryPoint = EntryPointAccessors.fromApplication(
            safeContext.applicationContext,
            ReceiverEntryPoint::class.java
        )
        val helper=entryPoint.provideMessageNotificationHelper()
        val sendMessageUseCaseById=entryPoint.provideSendMessageUseCase()
        val notifySendMessageUseCase=entryPoint.provideNotifySendMessageUseCase()
        val currentUser=Firebase.auth.currentUser

        val pendingResult = goAsync()
        CoroutineScope(Dispatchers.IO).launch {
//            val result=sendMessageUseCaseById.execute(
//                message = Message(
//                    senderId = currentUser!!.uid,
//                    text = textMessage
//                ),
//                chatId = extrasData.roomId!!
//            )
//            when(result){
//                is Resource.Error -> {}
//                is Resource.Loading -> {}
//                is Resource.Success -> {}
//            }
            pendingResult.finish()
        }

    }
    fun test(){
//        Log.d(TAG,"Activate: ReplyMessageReceiver")
//        val person= Person.Builder()
//            .setName("Вы")
//            .build()
//        val style = NotificationCompat.MessagingStyle(person)
//            .addMessage(textMessage,System.currentTimeMillis(),person)
//
//        if (ActivityCompat.checkSelfPermission(
//                safeContext,
//                Manifest.permission.POST_NOTIFICATIONS
//            ) != PackageManager.PERMISSION_GRANTED
//        ) {
//            return
//        }

//            NotificationManagerCompat.from(currentContext)
//                .notify(2,
//                    helper.createNotificationWithReply(NEW_MESSAGE_CHANNEL_ID,"","")
//                    .setStyle(style)
//                   // .setContentTitle("Отправлено!")
//                    .build()
//                )

    }

}