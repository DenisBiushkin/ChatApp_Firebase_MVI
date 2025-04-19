package com.example.unmei.android_frameworks.receiver


import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.Person
import androidx.core.app.RemoteInput
import androidx.core.graphics.drawable.IconCompat

import com.example.unmei.R
import com.example.unmei.android_frameworks.receiver.model.NotificationDataExtra
import com.example.unmei.android_frameworks.notification.MessageNotificationHelper
import com.example.unmei.util.ConstansApp.KEY_TEXT_REPLY
import com.example.unmei.util.ConstansApp.NOTIFICATION_ID_PENDING_EXTRAS
import com.example.unmei.util.ConstansDev.TAG
import javax.inject.Inject


class ReplyMessageReceiver @Inject constructor(
   // private val sendMessageUseCase: NotifySendMessageUseCase
):BroadcastReceiver() {


    override fun onReceive(context: Context?, intent: Intent?) {

        val remoteInput = intent?.let { RemoteInput.getResultsFromIntent(it) } ?: return
        val safeContext = context ?: return
        val json = intent.getStringExtra(NOTIFICATION_ID_PENDING_EXTRAS) ?: return

        val data = NotificationDataExtra.fromJson(json)
        val helper= MessageNotificationHelper(safeContext)
        val textMessage=remoteInput.getCharSequence(KEY_TEXT_REPLY).toString()
        Log.d(TAG,"Activate: ReplyMessageReceiver")
        val person= Person.Builder()
            .setName("Вы")
            .build()
        val style = NotificationCompat.MessagingStyle(person)
            .addMessage(textMessage,System.currentTimeMillis(),person)

        if (ActivityCompat.checkSelfPermission(
                safeContext,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }

//            NotificationManagerCompat.from(currentContext)
//                .notify(2,
//                    helper.createNotificationWithReply(NEW_MESSAGE_CHANNEL_ID,"","")
//                    .setStyle(style)
//                   // .setContentTitle("Отправлено!")
//                    .build()
//                )

    }

}