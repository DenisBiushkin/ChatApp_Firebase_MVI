package com.example.unmei.presentation.conversation_future.components

import android.net.Uri
import android.util.Log
import androidx.activity.compose.BackHandler

import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandIn
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectVerticalDragGestures

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi


import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet



import androidx.compose.material3.Scaffold

import androidx.compose.material3.SheetState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState


import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter

import com.example.unmei.presentation.conversation_future.ContentResolverClient
import com.example.unmei.presentation.conversation_future.model.ContentStateScreen
import com.example.unmei.presentation.conversation_future.model.ConversationEvent
import com.example.unmei.presentation.conversation_future.model.ConversationVMState
import com.example.unmei.presentation.conversation_future.model.MessageType
import com.example.unmei.presentation.conversation_future.utils.BaseRowWithSelectItemMessage
import com.example.unmei.presentation.conversation_future.utils.BottomButtonSelectMedia

import com.example.unmei.presentation.conversation_future.utils.ChatBubbleImages
import com.example.unmei.presentation.conversation_future.utils.ChatBubbleWithPattern
import com.example.unmei.presentation.conversation_future.utils.LoadingCircleProgressNewMessages
import com.example.unmei.presentation.conversation_future.utils.MessageContent
import com.example.unmei.presentation.conversation_future.viewmodel.ConversationViewModel
import com.example.unmei.presentation.util.ui.theme.chatBacgroundColor
import com.example.unmei.presentation.util.ui.theme.colorApp
import com.example.unmei.util.ConstansDev.TAG
import kotlin.math.roundToInt

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
                onOffClickOptions={viewModel.onEvent(ConversationEvent.Offoptions)},
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
                    }
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