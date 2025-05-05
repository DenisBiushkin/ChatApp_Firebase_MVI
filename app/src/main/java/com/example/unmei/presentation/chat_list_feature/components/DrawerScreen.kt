package com.example.unmei.presentation.chat_list_feature.components

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.unmei.presentation.chat_list_feature.viewmodel.ChatListViewModel
import kotlinx.coroutines.launch
import com.example.unmei.presentation.Navigation.Screens
import com.example.unmei.presentation.sign_in_feature.sign_in.GoogleAuthUiClient
import com.example.unmei.util.ConstansDev.TAG

@Preview(showBackground = true)
@Composable
fun showDrawerScreen(){
    //DrawerScreen(rememberNavController())
    DrawerContentFull(
        navController= rememberNavController(),
        iconOnClick = {},
        fullName = "",
        iconUrl = "",
        signInData ="",
        onClickItemDrawer = {
            //navController.navigate(it.navRoute)
        },
        onClickExitAccount = {

        }
    )
}

@Composable
fun DrawerScreen(
  navController: NavController,
  viewmodel: ChatListViewModel = hiltViewModel(),
  googleAuthUiClient: GoogleAuthUiClient
) {

    val state = viewmodel.state.collectAsState()
    val drawerState = rememberDrawerState(
        initialValue = DrawerValue.Closed
    )
    val scope = rememberCoroutineScope()
    val scopeExitAccount = rememberCoroutineScope()
   // viewmodel.observeUser()
    //заменить потом на Surface
    Box(
      modifier = Modifier.fillMaxSize()
    ) {
        ModalNavigationDrawer(
            drawerState = drawerState,
            drawerContent = {
                ModalDrawerSheet {

                    DrawerContentFull(
                        navController=navController,
                        iconOnClick = {

                            navController.navigate(Screens.Profile.withJsonData(state.value.userId))

                        },
                        fullName = state.value.fullName,
                        iconUrl = state.value.iconUrl,
                        signInData = state.value.signInData,
                        onClickItemDrawer = {
                            if(it.navRoute==Screens.Profile.route){
                                navController.navigate(
                                    Screens.Profile.withJsonData(state.value.userId)
                                )
                            }else{
                                navController.navigate(it.navRoute)
                            }

                        },
                        onClickExitAccount = {
                            Log.d(TAG,"Осуществляется выход")
                            scopeExitAccount.launch {
                                googleAuthUiClient.signOut()
                                //navController.clearBackStack(Screens.SignIn.route)
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
                      },
                      onCreateGroupClick = {
//                          navController.navigate(
//                              Screens.Chat.withExistenceData(
//                                  chatExist = true,
//                                  chatName = "TESTING",
//                                  chatUrl = "",
//                                  chatUid = "TESTING",
//                                  companionUid = ""
//                              )
//                          )
                          navController.navigate(Screens.CreateGroupFirst.withUserId(state.value.userId))
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


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DrawerTopBar(
    onOpenDrawer: ()->Unit,
    onCreateGroupClick:()->Unit
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
            IconButton(onClick = onCreateGroupClick) {
                Icon(
                    modifier = Modifier,
                    imageVector = Icons.Default.Add,
                    contentDescription = ""
                )
            }

        }

    )
}


