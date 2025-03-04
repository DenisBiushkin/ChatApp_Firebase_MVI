package com.example.unmei.presentation.conversation_future.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.unmei.R
import com.example.unmei.presentation.conversation_future.model.ConversationVMState
import com.example.unmei.presentation.conversation_future.utils.CustomMessageBubble
import com.example.unmei.presentation.conversation_future.utils.TimeMessage
import com.example.unmei.presentation.conversation_future.utils.BottomBarChatScreen
import com.example.unmei.presentation.conversation_future.viewmodel.ConversationViewModel
import com.example.unmei.presentation.util.ui.theme.chatBacgroundColor

@Preview(showBackground = true)
@Composable
fun showChatScreen(){
   ChatScreen(
       navController = rememberNavController(),
       viewModel = hiltViewModel()
   )
}
@Composable
fun  ChatScreen(
    navController: NavController,
    viewModel: ConversationViewModel,
) {

    val state = viewModel.state.collectAsState()
    val lazyState = rememberLazyListState()

    Scaffold(
        topBar = {
            TopBarChatScreen(
                onClickBack ={navController.popBackStack()},
                onClickProfile = {},
                iconChatPainter =  painterResource(id = R.drawable.test_user),
                titleChat = "Unknown",
                statusChat = "offline"
            )
        },
        bottomBar = {

            BottomBarChatScreen(
              viewModel=viewModel,
                lazyState = lazyState,
                vmState = state
            )
        },
        //учитывает появление клавиатуры
        modifier = Modifier.imePadding()
    ) {
        paddingValues ->
        ContentChatScreen(
            modifier = Modifier.padding(paddingValues),
            state = state,
            lazyState= lazyState
        )
    }
}
@Composable
fun ContentChatScreen(
    modifier: Modifier= Modifier,
    state: State<ConversationVMState>,
    lazyState: LazyListState
) {


    var previousIndex by remember { mutableStateOf(0) }
    val isScrolling by remember {
        //derivedStateOf оптимизирует измение,меняется только при реальном измении isScrolling а не просто recomposition
      derivedStateOf { lazyState.isScrollInProgress }
    }
    val isScrollingUp by remember {
        derivedStateOf {
            val isUp = (lazyState.firstVisibleItemIndex <previousIndex)
                    ||
                    (!lazyState.canScrollForward)//не можем больше пролестнуть вверх
            previousIndex =lazyState.firstVisibleItemIndex
            isUp
        }
    }
    println("Идет скрол "+isScrolling)
    println("Пользователь листает вверх "+isScrollingUp)

    LaunchedEffect(key1 =Unit) {
         lazyState.animateScrollToItem(4)
    }

    LazyColumn(
        state = lazyState,
        modifier = modifier
            .fillMaxSize()
            .background(color = chatBacgroundColor)
            .padding(start = 4.dp, end = 4.dp)
        ,
        //reverseLayout = true, // Новые сообщения появляются внизу
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
//        item {
//            TimeMessage(
//            )
//        }
        items( state.value.listMessage ) { message ->
            CustomMessageBubble(message)
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarChatScreen(
    modifier: Modifier = Modifier,
    onClickBack:()->Unit,
    onClickProfile: () -> Unit,
    statusChat:String,
    titleChat:String,
    iconChatPainter:Painter

){
    val colorApp= Color(0xFF42A5F5)
    val colorText= Color.White
    val colorTextLight = Color(0xFF90CAF9)

    val  expanded = remember { mutableStateOf(false) }
    TopAppBar(
        modifier = Modifier
            .fillMaxWidth()
        , title = {
            Row(
                modifier = Modifier
                    .padding(start = 5.dp)
                    .clickable { onClickProfile() }
                ,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Image(
                    modifier = Modifier
                        .clip(CircleShape)
                        .size(50.dp)
                    , painter =iconChatPainter,
                    contentDescription =""
                )
                Column (
                    modifier = Modifier.padding(start = 10.dp)
                ){
                    //отредачить расстояние между текстами
                    //см проект ComposeTraining
                    Text(
                        text = titleChat,
                        color=colorText,
                        maxLines = 1,
                        fontSize = 20.sp
                    )
                    //Жестко отредачить и добавить анимацию когда кто то печатает
                    Text(
                        modifier = Modifier.padding(top = 0.dp),

                        text = statusChat,
                        color= colorTextLight,
                        maxLines = 1,
                        fontSize = 15.sp
                    )
                }
            }
        },
        navigationIcon = {
            IconButton(
                modifier = Modifier
                    .padding(start = 5.dp)
                    ,
                onClick = { onClickBack() }
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    tint = Color.White,
                    contentDescription = null
                )
            }
        },
        //DropDown меню
        actions = {
            IconButton(  modifier = Modifier
                .padding(end = 2.dp),
                onClick = { expanded.value = true }
            ) {
                Icon(
                    tint = Color.White,
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = "Показать меню"
                )
            }
            DropdownMenu(
                expanded = expanded.value,
                onDismissRequest = { expanded.value = false }
            ) {
                Text("Скопировать", fontSize=18.sp, modifier = Modifier.padding(10.dp))
                Text("Вставить", fontSize=18.sp, modifier = Modifier.padding(10.dp))
                Divider()
                Text("Настройки", fontSize=18.sp, modifier = Modifier.padding(10.dp))
            }
        },
        colors= TopAppBarDefaults.topAppBarColors(
            containerColor = colorApp
        )
    )
}
@Composable
fun MessageBubble(message: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
            .background(
                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                shape = RoundedCornerShape(8.dp)
            )
            .padding(8.dp)
    ) {
        Text(text = message, style = MaterialTheme.typography.bodyMedium)
    }
}