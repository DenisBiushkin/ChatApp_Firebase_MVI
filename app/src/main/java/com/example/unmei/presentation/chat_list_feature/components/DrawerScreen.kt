package com.example.unmei.presentation.chat_list_feature.components

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.unmei.presentation.chat_list_feature.viewmodel.ChatListViewModel
import kotlinx.coroutines.launch
import com.example.unmei.R
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.unmei.domain.model.Status
import com.example.unmei.presentation.Navigation.Screens
import com.example.unmei.presentation.chat_list_feature.model.ChatListItemUI
import com.example.unmei.presentation.chat_list_feature.model.MessageStatus
import com.example.unmei.presentation.chat_list_feature.model.NotificationMessageStatus
import com.example.unmei.presentation.sign_in_feature.sign_in.GoogleAuthUiClient
import com.example.unmei.util.ConstansDev.TAG

@Preview(showBackground = true)
@Composable
fun showDrawerScreen(){
    //DrawerScreen(rememberNavController())
    DrawerContent(rememberNavController(), onClickExitAccount = {

    })
}

@Composable
fun DrawerScreen(
  navController: NavController,
  viewmodel: ChatListViewModel = hiltViewModel(),
  googleAuthUiClient: GoogleAuthUiClient
) {
    val drawerState = rememberDrawerState(
        initialValue = DrawerValue.Closed
    )
    val scope = rememberCoroutineScope()
    val scopeExitAccount = rememberCoroutineScope()
    viewmodel.observeUser()
    //заменить потом на Surface
    Box(
      modifier = Modifier.fillMaxSize()
    ) {
        ModalNavigationDrawer(
            drawerState = drawerState,
            drawerContent = {
                ModalDrawerSheet {
                    DrawerContent(
                        navController=navController,
                        onClickExitAccount = {
                            Log.d(TAG,"Осуществляется выход")
                            scopeExitAccount.launch {
                                googleAuthUiClient.signOut()
                                navController.clearBackStack(Screens.SignIn.route)
                                navController.navigate(Screens.SignIn.route)
                            }

                        }
                    )
                }
            },
            scrimColor = Color.Black.copy(alpha = 0.32f)
        ) {
          Scaffold(
              topBar = {
                  DrawerTopBar(
                      onOpenDrawer = {
                          scope.launch {
                              drawerState.apply {
                                  if (isOpen) close() else open()
                              }
                          }
                      }
                  )
              }
          ) { paddingValues ->
               ScreenContent(
                   navController = navController,
                   modifier = Modifier.padding(paddingValues),
                   viewmodel=viewmodel
               )

          }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun ScreenContent(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewmodel: ChatListViewModel
) {
    val state = viewmodel.state.collectAsState()
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    val showBottomSheet = remember {
        mutableStateOf(false)
    }
        Box(
            modifier = modifier,
            contentAlignment = Alignment.Center
        ) {
//        Button(onClick = { viewmodel.sendCommand() }) {
//            Text(text = "Click send to server")
//        }
          //  val list =state.value.chatList
            LazyColumn {
                if(state.value.isLoading){

                    items(10) {
                        AnimatedShimmerEffectChatItem()
                    }
                }else{
                    items(state.value.chatListAdv) {
                            it->
                        val text = if (!it.summaries.typingUsersStatus.isEmpty()){
                            "Печатает...."
                        }else{
                            if (it.summaries.lastMessage ==null) "" else it.summaries.lastMessage.text.toString()
                        }
                        val item = ChatListItemUI(
                            messageStatus = MessageStatus.Send,
                            notificationMessageStatus = NotificationMessageStatus.On,
                            isOnline = it.status.status == Status.ONLINE,
                            fullName = it.chatRoom.chatName,//Marcile Donato
                            painterUser = rememberAsyncImagePainter(model =  it.chatRoom.iconUrl),
                            messageText = text ,
                            //пока что String
                            timeStamp =  it.chatRoom.timestamp,
                            groupUid = it.chatRoom.id,
                            companionUid = "12"
                        )
                        ChatItem(
                            item = item,
                            onClick = {
                                navController.navigate(Screens.Chat.withInfo(groupUid = it.groupUid, companionUid = it.companionUid!!))
                            },
                            onLongClick = {
                                //  Log.d(TAG,"Long click")
                                showBottomSheet.value= true

                            }
                        )
                    }
                }

            }

            if (showBottomSheet.value) {
                ModalBottomSheet(
                    dragHandle = {

                    },
                    sheetState = sheetState,
                    onDismissRequest = {
                        showBottomSheet.value = false
                    }
                ) {

                    Box(modifier = Modifier.height(200.dp)){

                    }
                }
            }
        }

}




@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheetChatList(

){


}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DrawerTopBar(
    onOpenDrawer: ()->Unit
) {
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer
        ),
        title = {
            Text(
                modifier = Modifier
                    .padding(start = 14.dp),
                text = "FireWay"
            )
                },
        navigationIcon = {
            Icon(
                modifier = Modifier
                    .padding(start = 16.dp)
                    .clickable {
                        onOpenDrawer()
                    },
                imageVector =  Icons.Default.Menu,
                contentDescription =""
            )
        },
        actions = {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = ""
            )
        }

    )
}

@Composable
fun DrawerContent(
    navController: NavController,
    onClickExitAccount:()->Unit
) {

    val colorProfile = Color(0xFF42A5F5)
    val colorText = Color(0xFFFFFFFF)
    val colorTextLight = Color(0xFF90CAF9)
    //90CAF9 - светлый
        //AED6F1 - теплый

   val items = listOf(
       Icons.Default.Settings,

   )
    Column(
        modifier= Modifier
            .fillMaxHeight()
            //hard code для //xiaomi mi 9 se
            .width(LocalConfiguration.current.screenWidthDp.dp * 0.85f)
            .verticalScroll(rememberScrollState())
        ,

    ) {
        //блок аватарки
        Box(
            modifier= Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.3f)
                .background(color = colorProfile)
        ){
          Column (
              modifier= Modifier
                  .padding(start=15.dp,top=20.dp)
              ,
              //horizontalAlignment = Alignment.Start
          ){
            val imageModifier= Modifier
                .size(90.dp)
                .clip(CircleShape)
              //фото пользователя
            Image(
                modifier = imageModifier
                , painter = painterResource(id = R.drawable.test_user),
                contentDescription ="",
                contentScale = ContentScale.Crop//обрезает изображение по центру в доступное пространство
            )
              Spacer(modifier = Modifier.height(10.dp))
              Text(
                  text = "Марсиль Донато",
                  //fontFamily = lexendFontFamily,
                  fontWeight = FontWeight.Medium,
                  fontSize = 18.sp,
                  color=colorText
              )
              Spacer(modifier =Modifier.height(6.dp) )
              Text(
                  text = "+7 927 163 37 09",
                  //fontFamily = lexendFontFamily,
                  fontWeight = FontWeight.Normal,
                  fontSize = 16.sp,
                  color = colorTextLight
              )
          }

        }
        //блок драйвера
        data class NavigationItem(
            val title:String,
            val imageVector:ImageVector,
            val navRoute:String="",
            val colorIcon:Color= Color.Black.copy(alpha = 0.5f)
        )
        val navigationItems= listOf(
            NavigationItem(
                title="Профиль",
                imageVector= ImageVector.vectorResource(id = R.drawable.account_circle_24px)
            ),
            NavigationItem(
                title="Группы",
                imageVector= ImageVector.vectorResource(id = R.drawable.group_24px),
                navRoute = Screens.Chat.route
            ),
            NavigationItem(
                title="Настройки",
                imageVector= ImageVector.vectorResource(id = R.drawable.settings_24px),
                navRoute = Screens.Main.route
            ),


        )
        Box(
            modifier= Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.7f)
        ){
            Column {
                navigationItems.forEachIndexed {
                        index, it->
                    if(index==2){
                        HorizontalDivider()
                    }
                    NavigationDrawerItem(
                        label = {
                            Text(
                                text = it.title,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Normal,

                                )
                        },
                        selected = false,
                        onClick = {
                            navController.navigate(it.navRoute)
                        },
                        icon = {
                            Icon(
                                modifier = Modifier.width(30.dp),
                                imageVector = it.imageVector,
                                contentDescription ="",
                                tint = it.colorIcon
                            )

                        },
                        modifier = Modifier
                            .padding(NavigationDrawerItemDefaults.ItemPadding)
                    )
                }
                val exitItem = NavigationItem(
                    title="Выход",
                    imageVector=ImageVector.vectorResource(id = R.drawable.exit_to_app_24px),
                    navRoute = Screens.Main.route,
                    colorIcon = Color.Red
                )
                NavigationDrawerItem(
                    label = {
                        Text(
                            text = exitItem.title,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Normal,
                            color = exitItem.colorIcon
                            )
                     }, selected =false ,
                    icon = {
                        Icon(
                            modifier = Modifier.width(30.dp),
                            imageVector = exitItem.imageVector,
                            contentDescription ="",
                            tint = exitItem.colorIcon
                        )
                    },
                    onClick = onClickExitAccount,
                    modifier = Modifier
                        .padding(NavigationDrawerItemDefaults.ItemPadding)
                )
            }

        }

    }

   // NavigationDrawerItem(label = { /*TODO*/ }, selected =false , onClick = { /*TODO*/ })
}
