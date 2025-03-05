package com.example.unmei.presentation.conversation_future.components

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.unmei.R
import com.example.unmei.presentation.conversation_future.model.ConversationEvent
import com.example.unmei.presentation.conversation_future.model.ConversationVMState
import com.example.unmei.presentation.conversation_future.utils.CustomMessageBubble
import com.example.unmei.presentation.conversation_future.utils.BottomBarChatScreen
import com.example.unmei.presentation.conversation_future.utils.TopBarChatScreen
import com.example.unmei.presentation.conversation_future.viewmodel.ConversationViewModel
import com.example.unmei.presentation.util.ui.theme.chatBacgroundColor
import com.example.unmei.presentation.util.ui.theme.colorApp
import com.example.unmei.util.ConstansDev.TAG

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
            lazyState= lazyState,
            viewModel
        )
    }
}
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ContentChatScreen(
    modifier: Modifier= Modifier,
    state: State<ConversationVMState>,
    lazyState: LazyListState,
    viewModel: ConversationViewModel
) {
    var previousIndex by remember { mutableStateOf(0) }
    val isScrolling by remember {
        //derivedStateOf оптимизирует измение,меняется только при реальном измении isScrolling а не просто recomposition
        derivedStateOf { lazyState.isScrollInProgress }
    }
    val isScrollingUp by remember {
        derivedStateOf {
            val isUp = (lazyState.firstVisibleItemIndex <previousIndex)
                 //   ||
                 //   (!lazyState.canScrollForward)//не можем больше пролестнуть вверх
            previousIndex =lazyState.firstVisibleItemIndex
            isUp
        }
    }
    val canScrollForward by remember {
        derivedStateOf {
            lazyState.canScrollForward
        }
    }
    Log.d(TAG,"canScrollForward $canScrollForward")
//    if(isScrollingUp && !state.value.loadingOldMessages){
//
//        Log.d(TAG,"Загрузка старых")
//        viewModel.onEvent(ConversationEvent.LoadingNewMessage)
//    }
    println("Идет скрол "+isScrolling)
    println("Пользователь листает вверх "+isScrollingUp)

    if (state.value.loadingScreen){
        CircularProgressIndicator()
    }else{

        LazyColumn(
            state = lazyState,
            modifier = modifier
                .fillMaxSize()
                .background(color = chatBacgroundColor)
                .padding(start = 4.dp, end = 4.dp)
            ,
         //   reverseLayout = true,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            item{
                if(state.value.loadingOldMessages){
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)

                        , horizontalArrangement = Arrangement.Center
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .size(30.dp)
                            ,
                            color = colorApp
                        )
                    }
                }
            }
            items( state.value.listMessage ) { message ->
                CustomMessageBubble(
                    itemUI = message,
                    modifier = Modifier,
                    optionVisibility = state.value.optionsVisibility,
                    onClickLine = {
                          if (state.value.optionsVisibility){
                              viewModel.onChangeSelectedMessages(messageId = message.messageId)
                          }
                    },
                    onLongClickLine = {
                        viewModel.onChangeSelectedMessages(messageId = message.messageId)
                    },
                    selected = state.value.selectedMessages[message.messageId] == true

                )
                Spacer(modifier = Modifier.height(8.dp))
            }

        }
    }



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