package com.example.unmei.receiver


import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.Person
import androidx.core.app.RemoteInput
import androidx.core.graphics.drawable.IconCompat

import com.example.unmei.R
import com.example.unmei.util.MessageNotificationHelper
import com.example.unmei.util.ConstansApp.KEY_TEXT_REPLY
import com.example.unmei.util.ConstansApp.NEW_MESSAGE_CHANNEL_ID
import com.example.unmei.util.ConstansDev.TAG


class ReplyMessageReceiver :BroadcastReceiver() {


    override fun onReceive(context: Context?, intent: Intent?) {

        val remoteInput = intent?.let { RemoteInput.getResultsFromIntent(it) }

        Log.d(TAG,"Activate: ReplyMessageReceiver")
        if (remoteInput != null) {
            val textMessage=remoteInput.getCharSequence(KEY_TEXT_REPLY).toString()
            val currentContext=context!!.applicationContext
            val helper= MessageNotificationHelper(currentContext)

            val person= Person.Builder()
                .setIcon(IconCompat.createWithResource(context, R.drawable.tohsaka))
                .setName("Вы")
                .build()


            val style = NotificationCompat.MessagingStyle(person)
                .addMessage(textMessage,System.currentTimeMillis(),person)

            if (ActivityCompat.checkSelfPermission(
                    currentContext,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return
            }

            NotificationManagerCompat.from(currentContext)
                .notify(2,
                    helper.createNotification(NEW_MESSAGE_CHANNEL_ID)
                    .setStyle(style)
                   // .setContentTitle("Отправлено!")
                    .build()
                )

        }
    }

    private fun createSimpleMessageNotification(
       message:String,
       context: Context,
       channelId:String
    ): NotificationCompat.Builder {
        val person= Person.Builder()
            .setName("Вы")
            .build()
        val style = NotificationCompat.MessagingStyle(person)
            .addMessage(message,System.currentTimeMillis(),person)
        return  NotificationCompat.Builder(context,channelId)
                 .setSmallIcon(R.drawable.ic_launcher_foreground)
                 .setStyle(style)
    }
}