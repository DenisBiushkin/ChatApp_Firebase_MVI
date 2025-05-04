package com.example.unmei.presentation.singleChat_feature.components

import androidx.activity.compose.BackHandler

import androidx.annotation.RequiresApi
import androidx.compose.foundation.background

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi


import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme


import androidx.compose.material3.Scaffold

import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState


import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter

import com.example.unmei.presentation.singleChat_feature.model.ContentStateScreen
import com.example.unmei.presentation.singleChat_feature.model.ConversationEvent

import com.example.unmei.presentation.singleChat_feature.viewmodel.ConversationViewModel
import com.example.unmei.presentation.util.ui.theme.chatBacgroundColor
import kotlinx.coroutines.flow.distinctUntilChanged
import okio.AsyncTimeout.Companion.condition

@RequiresApi(35)
@Preview(showBackground = true)
@Composable
fun showChatScreen(){
   ChatScreen(
       navController = rememberNavController(),
       viewModel = hiltViewModel()
   )
}


@RequiresApi(35)
@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun  ChatScreen(
    navController: NavController,
    viewModel: ConversationViewModel,
) {
    val state = viewModel.state.collectAsState()
    val lazyState = rememberLazyListState()
    val bottomSheetState = rememberModalBottomSheetState()

    BackHandler {
        viewModel.onEvent(ConversationEvent.LeftChat)
        navController.popBackStack()
    }
    Scaffold(
        topBar = {
            TopBarChatScreen(
                onClickBack ={
                    viewModel.onEvent(ConversationEvent.LeftChat)
                    navController.popBackStack()
                 }
                ,onClickProfile = {},
                iconChatPainter = rememberAsyncImagePainter(model =state.value.chatIconUrl),
                titleChat = state.value.chatFullName,
                statusChat = state.value.statusChat,
                isTyping=state.value.isTyping
            )
            TopBarMessageActions(
                isVisible=state.value.optionsVisibility,
                onOffClickOptions={viewModel.onEvent(ConversationEvent.OffOptions)},
                onDeleteClickMessages={viewModel.onEvent(ConversationEvent.DeleteSelectedMessages)}
            )
        },
        bottomBar = {
            ChatScreenBottomBar(
                textMessage = state.value.textMessage,
                onValueChangeTextMessage = { viewModel.onEvent(ConversationEvent.OnValueChangeTextMessage(it)) },
                onClickContent = { viewModel.onEvent(ConversationEvent.OpenCloseBottomSheet) },
                onClickSendMessage = { viewModel.onEvent(ConversationEvent.SendMessage) }
            )
        },
        modifier = Modifier
            .fillMaxSize()
    ) {
        paddingValues ->
        when(state.value.contentState) {
            is ContentStateScreen.Error ->{}
            ContentStateScreen.EmptyType -> {
                ChatScreenEmptyContent(
                    modifier = Modifier.padding(paddingValues),
                    backgroundColor = chatBacgroundColor
                )
            }
            ContentStateScreen.Loading -> {
                CircularProgressIndicator()
            }
            ContentStateScreen.Content -> {

                ContentChatScreen(
                    modifier = Modifier
                        .padding(paddingValues)
                        .consumeWindowInsets(paddingValues), lazyState = lazyState,
                    isLoadingOldMessages = state.value.loadingOldMessages,
                    listMessage = state.value.listMessage,
                    selectedMessages = state.value.selectedMessages,
                    optionsVisibility = state.value.optionsVisibility,
                    onClickMessageLine = {
                        if (state.value.optionsVisibility){
                            viewModel.onEvent(ConversationEvent.ChangeSelectedMessages(it))
                        }else{
                            //bottom drawer
                        }
                    },
                    onLongClickMessageLine = {
                       viewModel.onEvent(ConversationEvent.ChangeSelectedMessages(it))
                    },
                    groupedListMessage = state.value.grouped
                )
            }


        }
       // Log.d(TAG,state.value.selectedMessages.keys.toList().toString())
        ConversationModalBottom(
            bottomSheetState = bottomSheetState,
            visibility = state.value.bottomSheetVisibility,
            onDismissRequest = {
                viewModel.onEvent(ConversationEvent.OpenCloseBottomSheet)
            },
            onSelectedMedia = {
                viewModel.onEvent(ConversationEvent.SelectedMediaToSend(it))
            }
        )
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