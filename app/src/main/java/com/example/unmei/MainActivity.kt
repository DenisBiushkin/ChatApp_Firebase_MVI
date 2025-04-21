package com.example.unmei

import android.Manifest
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Icon
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.Person
import androidx.core.app.TaskStackBuilder
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.IconCompat
import androidx.core.net.toUri
import androidx.navigation.compose.rememberNavController
import com.example.unmei.domain.model.Message
import com.example.unmei.domain.model.RoomDetail
import com.example.unmei.domain.model.Status
import com.example.unmei.domain.model.StatusUser
import com.example.unmei.domain.model.TypeRoom
import com.example.unmei.domain.repository.NotificationRepository
import com.example.unmei.domain.usecase.user.SetStatusUserUseCase
import com.example.unmei.presentation.Navigation.HostNavGraph
import com.example.unmei.presentation.sign_in_feature.sign_in.GoogleAuthUiClient
import com.example.unmei.presentation.util.model.NavigateConversationData
import com.example.unmei.presentation.util.ui.theme.UnmeiTheme
import com.example.unmei.util.ConstansApp
import com.example.unmei.util.ConstansDev
import com.example.unmei.util.ConstansDev.TAG
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private lateinit var auth: FirebaseAuth


    @Inject
    lateinit var setStatusUserUseCase: SetStatusUserUseCase

    @Inject
    lateinit var notificationRepository: NotificationRepository

    private val googleAuthUiClient by lazy {
        GoogleAuthUiClient(
            context = applicationContext,
            oneTapClient = Identity.getSignInClient(applicationContext)
        )
    }

    override fun onStart() {
        super.onStart()
        CoroutineScope(Dispatchers.IO).launch {
            val status = StatusUser(
                Status.ONLINE,
                System.currentTimeMillis()
            )
            if(auth.currentUser !=null){
                setStatusUserUseCase.execute(auth.currentUser!!.uid,status)
            }

        }
    }
    override fun onStop() {
        super.onStop()
        CoroutineScope(Dispatchers.IO).launch {
            val status = StatusUser(
                Status.OFFLINE,
                System.currentTimeMillis()
            )
            if(auth.currentUser !=null) {
                setStatusUserUseCase.execute(auth.currentUser!!.uid, status)
            }
        }
    }
    //private val repositorty: MainRepositoryImpl = MainRepositoryImpl()
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()


        auth = Firebase.auth
        var startDestinationRoute = ConstansApp.AUTH_NAVIGATE_ROUTE
        val currentUser = auth.currentUser

        if(currentUser!=null){
            startDestinationRoute=ConstansApp.MAIN_NAVIGATE_ROUTE
             saveFCMToken(currentUser.uid)
        }

        //ПОПРАВИТЬ
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.POST_NOTIFICATIONS
            ),
            0
        )
        val channelId ="Message_Channel"
        val channel = createNotificationChannel(this,ConstansApp.NEW_MESSAGE_CHANNEL_ID,ConstansApp.MESSAGE_CHANNEL_NAME)
//        val notification= createStyleMessageNotification(this,channelId)
//
//        if (ActivityCompat.checkSelfPermission(
//                this,
//                Manifest.permission.POST_NOTIFICATIONS
//            ) != PackageManager.PERMISSION_GRANTED
//        ) {
//            // TODO: Consider calling
//            //    ActivityCompat#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for ActivityCompat#requestPermissions for more details.
//            return
//        }
//        channel.notify(12,notification)
//
//        CoroutineScope(Dispatchers.IO).launch {
//           // delay(5000)
//            //channel.cancel(12)
//        }

        setContent {
            UnmeiTheme {
                HostNavGraph(
                    navHostController = rememberNavController(),
                    startDestinationRoute=startDestinationRoute,
                    googleAuthUiClient=googleAuthUiClient
                )
            }
        }
    }

    //отредачить потом
    private fun saveFCMToken(userId:String){
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.d(TAG, "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }
            val token = task.result
            CoroutineScope(Dispatchers.IO).launch {
                notificationRepository.saveNotificationTokenByUserId(
                    token=token,
                    userid =userId
                )
            }
            val msg = token
            Log.d(TAG, "FCM Token "+msg)
        })
    }
}

@RequiresApi(Build.VERSION_CODES.P)
fun createStyleMessageNotification(
    context:Context,
    channelId:String,
): Notification {
   val data= NavigateConversationData(
        chatExist = true,
        chatUrl = "https://firebasestorage.googleapis.com/v0/b/repository-d6c1a.appspot.com/o/TestImages/Marcile_donato.jpg?alt=media&token=524e6685-cbdf-4bed-9da3-095e56832093",
        chatName = "Mercile Donato",
        companionUid="companionUid",
        chatUid = "-OLzxQapoxM2NawF_8nX"
    )

    val icon =IconCompat.createWithResource(context, R.drawable.erishkagel)
    val user = Person.Builder()
        .setName("Cha He In")
        .setIcon(icon) // или круглая bitmap
        .build()

    val message = NotificationCompat.MessagingStyle.Message(
        "Hey, we made this Pizza tonight :)",
        System.currentTimeMillis(),
        user
    )
    val style = NotificationCompat.MessagingStyle(user)
        .addMessage(message)
        .setConversationTitle("Cha He In") // или "Чат с Chance"

    val clickIntent = Intent(
        Intent.ACTION_VIEW,
        "${ConstansApp.CHAT_URI_DEEPLINK}/${data.toJson()}".toUri(),
        context,
        MainActivity::class.java
    )
val intent = Intent(context,MainActivity::class.java)

    val clickPendingIntent :PendingIntent = TaskStackBuilder.create(context).run {
        addNextIntentWithParentStack(clickIntent)
        getPendingIntent(1, PendingIntent.FLAG_IMMUTABLE)!!
    }

    return NotificationCompat.Builder(context, channelId)
        .setSmallIcon(R.drawable.chat_24px)
        //.addAction(0,"Открыть",pendingIntent )
        .setContentIntent(clickPendingIntent)
        .setColor(0x0000FF)
        .setStyle(style)
        .setLargeIcon(BitmapFactory.decodeResource(context.resources, R.drawable.erishkagel))
        .setColor(0x0000FF)
        .setNumber(3) // 🔵 это и есть badge-счётчик!
        .setAutoCancel(true)
        .build()
}



fun createNotificationChannel(
    context:Context,
    channelId: String,
    channelName:String
): NotificationManagerCompat {
    val notificationManager=NotificationManagerCompat.from(context)
    val channel = NotificationChannel(
        channelId,
        channelName,
        NotificationManager.IMPORTANCE_HIGH
    )
    notificationManager.createNotificationChannel(channel)
    return notificationManager
}
fun createMessageNotification(
    context:Context,
    channelId:String,
): Notification {
    return NotificationCompat.Builder(context,channelId)
        .setSmallIcon(R.drawable.chat_24px)
        .setColor(0x0000FF)
        .setContentTitle("Cha He In")
        .setContentText("Текст сообщения")
        .setLargeIcon(BitmapFactory.decodeResource(context.resources, R.drawable.erishkagel))
        .build()
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    UnmeiTheme {
        Greeting("Android")
    }
}