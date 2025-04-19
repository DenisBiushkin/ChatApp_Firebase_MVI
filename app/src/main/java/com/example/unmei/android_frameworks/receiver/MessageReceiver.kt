package com.example.unmei.android_frameworks.receiver

import android.Manifest
import android.app.Notification
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationManagerCompat
import com.example.unmei.data.model.FcmMessage
import com.example.unmei.domain.model.TypeRoom
import com.example.unmei.util.ConstansApp.MESSAGE_REC_KEY
import com.example.unmei.android_frameworks.notification.MessageNotificationHelper
import com.example.unmei.android_frameworks.notification.NotificationIdGenerator
import com.example.unmei.util.ConstansApp.MESSAGE_GROUP_KEY
import com.example.unmei.util.ConstansApp.MESSAGE_SUMMERY_ID
import com.example.unmei.util.ConstansApp.NEW_MESSAGE_CHANNEL_ID

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
//@AndroidEntryPoint
class MessageReceiver  @Inject constructor(

):BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        val safeContext = context ?: return
        val json = intent?.getStringExtra(MESSAGE_REC_KEY) ?: return

        val data = FcmMessage.fromJson(json).message.data
        val helper= MessageNotificationHelper(safeContext)
        val notificationId= NotificationIdGenerator.fromString(data.roomId)
        val notificationManager = NotificationManagerCompat.from(safeContext)




        val pendingResult = goAsync()

        //само по себе создание и отправка уведомлений
        // не требует основного потока (Dispatchers.Main), так как это не UI-операция
        CoroutineScope(Dispatchers.IO).launch {
            var notification: Notification
            when (data.typeRoom) {
                TypeRoom.PRIVATE.value ->{
                    notification=helper.createSingleMessageNotification(
                        notificationId = notificationId,
                        data = data
                    ).build()
                }
                TypeRoom.PUBLIC.value ->{
                    notification=helper.createSingleMessageNotification(
                        notificationId = notificationId,
                        data = data
                    ).build()
                }
                else -> return@launch
            }
            if (ActivityCompat.checkSelfPermission(
                    safeContext,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
            }
            if(!helper.isNotificationActive(MESSAGE_SUMMERY_ID)){
                val ntf=helper.summaryGroupNotification(NEW_MESSAGE_CHANNEL_ID,MESSAGE_GROUP_KEY).build()
                notificationManager.notify(MESSAGE_SUMMERY_ID,ntf)
            }
            notificationManager.notify(
                notificationId,
                notification
            )

            pendingResult.finish()
        }
    }

}