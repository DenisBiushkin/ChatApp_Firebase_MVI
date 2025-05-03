package com.example.unmei.presentation.groupChat_feature.components

import androidx.activity.compose.BackHandler
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Scaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberModalBottomSheetState

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.unmei.presentation.groupChat_feature.model.GroupChatScreenState
import com.example.unmei.presentation.groupChat_feature.viemodel.GroupChatViewModel
import com.example.unmei.presentation.singleChat_feature.components.ChatScreenBottomBar
import com.example.unmei.presentation.singleChat_feature.components.ContentChatScreen
import com.example.unmei.presentation.singleChat_feature.components.ConversationModalBottom
import com.example.unmei.presentation.singleChat_feature.components.TopBarChatScreen
import com.example.unmei.presentation.singleChat_feature.components.TopBarMessageActions
import com.example.unmei.presentation.singleChat_feature.model.ContentStateScreen
import com.example.unmei.presentation.singleChat_feature.model.ConversationEvent
import com.example.unmei.presentation.util.LoadingScreen
import kotlinx.coroutines.flow.distinctUntilChanged

@RequiresApi(35)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun  GroupChatScreen(
    navController: NavController,
    viewModel: GroupChatViewModel= hiltViewModel(),
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
                titleChat = state.value.chatName,
                statusChat = state.value.chatStatus,
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

            is ContentStateScreen.Content -> {
                LaunchedEffect(lazyState) {
                    snapshotFlow {
                        val lastVisible = lazyState.layoutInfo.visibleItemsInfo.lastOrNull()
                        val totalItems = lazyState.layoutInfo.totalItemsCount
                        lastVisible?.index == totalItems - 1
                    }
                    .distinctUntilChanged()
                    .collect { isAtEnd ->
                        if (isAtEnd && !state.value.onReached && !state.value.loadingOldMessages) {
                            viewModel.loadNextMessages()
                        }
                    }
                }
                ContentChatScreen(
                    modifier = Modifier
                        .padding(paddingValues)
                        .consumeWindowInsets(paddingValues),
                    lazyState = lazyState,
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
            is ContentStateScreen.EmptyType -> {}
            is ContentStateScreen.Error -> {}
            is ContentStateScreen.Loading -> { LoadingScreen()}



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
