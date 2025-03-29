package com.example.unmei.presentation.profile_user_feature.components

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.FabPosition
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import com.example.unmei.R
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.example.unmei.presentation.Navigation.Screens
import com.example.unmei.presentation.chat_list_feature.components.BoxWithImageUser
import com.example.unmei.presentation.profile_user_feature.viewmodel.ProfileUserViewModel
import com.example.unmei.presentation.util.ui.theme.colorApp
import com.example.unmei.util.ConstansDev.TAG





@Composable
fun ProfileUserFull(
    navController: NavController,
    viewmodel: ProfileUserViewModel
) {
   val state = viewmodel.state.collectAsState()
    ProfileUserScreen(
        backOnClick = {
            navController.popBackStack()
        },
        fullName = state.value.fullName,
        painterIcon = rememberAsyncImagePainter(model = state.value.iconUrl),
        statusOnline =state.value.statusOnline,
        isMine = state.value.isMine,
        messageOnClick={
            //TO DO !state.value.userId.isEmpty()
            if (false){
                Log.d(TAG,"Попали в условие для перехода")
                navController.navigate(
                    Screens.Chat.withExistenceData(
                        chatExist = false,
                        companionUid = state.value.userId,
                        chatName = state.value.fullName,
                        chatUrl = state.value.iconUrl
                    ))
            }
        }
    )

}

@Composable
fun BottomPart(

){
    Box(
        modifier = Modifier.fillMaxSize()

    ){
        Column(
            modifier = Modifier
                .padding(top = 20.dp, start = 20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Информация",
                fontWeight = FontWeight.Medium,
                fontSize = 16.sp,
                color = colorApp
            )
            BlockInfo(
                text = "@marcile",
                trailingText = "Имя пользователя"
            )
            BlockInfo(
                text = "@marcile",
                trailingText = "Имя пользователя"
            )
        }
    }
}
@Composable
fun BlockInfo(
    text:String,
    trailingText:String
){
  Column {
      Text(
          text = text,
          fontWeight = FontWeight.Normal,
          fontSize = 16.sp,
          color = Color.Black.copy(0.9f)
      )
      Spacer(modifier = Modifier.height(5.dp))
      Text(
          text = trailingText,
          fontWeight = FontWeight.Medium,
          fontSize = 12.sp,
          color = Color.Black.copy(0.5f)
      )
  }
}
@Composable
fun TopPart(
    backOnClick:()->Unit,
    fullName:String,
    statusOnline:String,
    painterIcon: Painter,
){
    val colorText = Color(0xFFFFFFFF)

    Box (
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.5f)
           // .background(Color.Blue),
        ,// contentAlignment = Alignment.BottomCenter
    ){
      Image(
          modifier = Modifier.matchParentSize(),
          painter = painterIcon ,
          contentScale = ContentScale.Crop,
          contentDescription =""
      )
      Column(
          modifier = Modifier.fillMaxSize(),
          verticalArrangement = Arrangement.SpaceBetween
      ) {
          //navigation
           TopAppBar(
               modifier = Modifier.padding(
                   top= 25.dp
               ),
               title = { /*TODO*/
               },
               backgroundColor = Color.Transparent,
               elevation = 0.dp,
               navigationIcon = {
                   Icon(
                       modifier = Modifier
                           .padding(start = 16.dp)
                           .clickable {
                               backOnClick()
                           },
                       imageVector =  Icons.Default.ArrowBack,
                       tint = Color.White,
                       contentDescription =""
                   )
               },
               actions = {
                   Icon(
                       modifier = Modifier
                           .padding(start = 16.dp)
                           .clickable {

                           },
                       imageVector=Icons.Default.MoreVert,
                       tint = Color.White,
                       contentDescription =""
                   )
               }
           )
          //Info User
         Row (
             modifier = Modifier
                 .padding(
                     horizontal = 20.dp,
                     vertical = 10.dp
                 )
             ,
             horizontalArrangement = Arrangement.Start,
             verticalAlignment = Alignment.CenterVertically
         ){
             Column (
                 verticalArrangement = Arrangement.spacedBy(7.dp)
             ){
                 Text(
                     text = fullName,
                     //fontFamily = lexendFontFamily,
                     fontWeight = FontWeight.Medium,
                     fontSize = 18.sp,
                     color = colorText
                 )
                 Text(
                     text =  statusOnline,
                     //fontFamily = lexendFontFamily,
                     fontWeight = FontWeight.Medium,
                     fontSize = 14.sp,
                     color = colorText.copy(0.6f)
                 )
             }

         }
      }

    }
}