package com.example.unmei.receiver

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.util.Log
import android.widget.Toast
import androidx.compose.material3.TopAppBarState
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.graphics.drawable.IconCompat
import com.example.unmei.R
import com.example.unmei.data.model.FcmData
import com.example.unmei.data.model.FcmMessage
import com.example.unmei.domain.model.TypeRoom
import com.example.unmei.util.ConstansApp.MESSAGE_REC_KEY
import com.example.unmei.util.ConstansApp.NEW_MESSAGE_CHANNEL_ID
import com.example.unmei.util.ConstansDev.TAG
import com.example.unmei.util.MessageNotificationHelper
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject
//@AndroidEntryPoint
class MessageReceiver  @Inject constructor(

):BroadcastReceiver() {

    private lateinit var  messageNotificationHelper: MessageNotificationHelper
    private lateinit var contextRec: Context

    override fun onReceive(context: Context?, intent: Intent?) {
        val safeContext = context ?: return
        contextRec=safeContext
        messageNotificationHelper = MessageNotificationHelper(safeContext)
        val json = intent?.getStringExtra(MESSAGE_REC_KEY) ?: return
        val data = FcmMessage.fromJson(json).message.data

        val pendingResult = goAsync()
        CoroutineScope(Dispatchers.IO).launch {
            val icon = try {

                messageNotificationHelper.loadBitmapFromUrl(safeContext, data.image ?: "")
            } catch (e: Exception) {
                null
            } ?: IconCompat.createWithResource(safeContext, R.drawable.erishkagel)

            when (data.typeRoom) {
                TypeRoom.PRIVATE.value -> notifySingleChat(data, icon)
                TypeRoom.PUBLIC.value -> notifyGroupChat(data)
                else -> Unit
            }

            Log.d(TAG, "Received Notification (MessageReceiver): $data")
            pendingResult.finish()
        }
    }
    private fun notifySingleChat(
        myFcmData:FcmData,
        iconCompat: IconCompat?
    ){
            Log.d(TAG,"Notify Single Chat")
        val notification= messageNotificationHelper.createNotificationWithReply(
            channelId = NEW_MESSAGE_CHANNEL_ID,
            title = myFcmData.title,
            body = myFcmData.body,
            icon =iconCompat!!
        )
        if (ActivityCompat.checkSelfPermission(
                contextRec,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        NotificationManagerCompat.from(contextRec).notify(
            12,
            notification.build()
        )
    }
    private fun notifyGroupChat(myFcmData: FcmData){
        Log.d(TAG,"Notify Group Chat")

    }
}