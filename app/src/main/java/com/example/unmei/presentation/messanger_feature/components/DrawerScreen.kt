package com.example.unmei.presentation.messanger_feature.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.unmei.presentation.messanger_feature.viewmodel.ChatsViewModel
import kotlinx.coroutines.launch

@Preview(showBackground = true)
@Composable
fun showDrawerScreen(){
    DrawerScreen(rememberNavController())
}

@Composable
fun DrawerScreen(
  navController: NavController,
  viewmodel: ChatsViewModel = hiltViewModel()
) {
    val drawerState = rememberDrawerState(
        initialValue = DrawerValue.Closed
    )
    val scope = rememberCoroutineScope()


    Surface(
      
    ) {
        ModalNavigationDrawer(
            drawerState = drawerState,
            drawerContent = {
                ModalDrawerSheet {
                    DrawerContent()
                }
            }
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
    viewmodel: ChatsViewModel
) {

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ){
        Button(onClick = { viewmodel.sendCommand() }) {
            Text(text = "Click send to server")
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
fun DrawerContent() {
    Text(text = "")
    HorizontalDivider()
   // NavigationDrawerItem(label = { /*TODO*/ }, selected =false , onClick = { /*TODO*/ })
}
