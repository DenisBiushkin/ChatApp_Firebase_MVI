package com.example.unmei.presentation.messanger_feature.components

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.unmei.presentation.messanger_feature.viewmodel.ChatListViewModel
import kotlinx.coroutines.launch
import com.example.unmei.R
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.unmei.presentation.Navigation.Screens

@Preview(showBackground = true)
@Composable
fun showDrawerScreen(){
    //DrawerScreen(rememberNavController())
    DrawerContent(rememberNavController())
}

@Composable
fun DrawerScreen(
  navController: NavController,
  viewmodel: ChatListViewModel = hiltViewModel()
) {
    val drawerState = rememberDrawerState(
        initialValue = DrawerValue.Closed
    )
    val scope = rememberCoroutineScope()

//заменить потом на Surface
    Box(
      modifier = Modifier.fillMaxSize()
    ) {
        ModalNavigationDrawer(
            drawerState = drawerState,
            drawerContent = {
                ModalDrawerSheet {
                    DrawerContent(navController=navController)
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
                   Modifier.padding(paddingValues),
                   viewmodel=viewmodel
               )

          }
        }
    }
}

@Composable
fun ScreenContent(
    modifier: Modifier = Modifier,
    viewmodel: ChatListViewModel
) {

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ){
//        Button(onClick = { viewmodel.sendCommand() }) {
//            Text(text = "Click send to server")
//        }
        LazyColumn {
            items(count = 1){
                ChatItem()
            }
        }
    }
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
fun DrawerContent(navController: NavController) {

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
            val painter:Painter,
            val navRoute:String=""
        )
        val navigationItems= listOf(
            NavigationItem(
                title="Профиль",
                painter= painterResource(id = R.drawable.profile_icon)
            ),
            NavigationItem(
                title="Группы",
                painter= painterResource(id = R.drawable.users_icon),
                navRoute = Screens.Test.route
            ),
            NavigationItem(
                title="Настройки",
                painter= painterResource(id = R.drawable.setting_icon),
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
                                painter = it.painter,
                                contentDescription =""
                            )

                        },
                        modifier = Modifier
                            .padding(NavigationDrawerItemDefaults.ItemPadding)
                    )
                }
            }

        }

    }

   // NavigationDrawerItem(label = { /*TODO*/ }, selected =false , onClick = { /*TODO*/ })
}
