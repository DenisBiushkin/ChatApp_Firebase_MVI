package com.example.unmei.presentation.chat_list_feature.components
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet

import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color

import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.unmei.R
import com.example.unmei.presentation.Navigation.Screens
import com.example.unmei.presentation.chat_list_feature.model.TypingStatus
import com.example.unmei.presentation.chat_list_feature.viewmodel.ChatListViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun ScreenContent(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewmodel: ChatListViewModel
) {
    val state = viewmodel.state.collectAsState()
    val sheetState = rememberModalBottomSheetState()
    val showBottomSheet = remember {
        mutableStateOf(false)
    }
        Box(
            modifier = modifier,
            contentAlignment = Alignment.Center
        ) {
            LazyColumn {
                if(state.value.isLoading){
                    items(10) {
                        AnimatedShimmerEffectChatItem()
                    }
                }else{
                    items(state.value.chatListAdv) {
                        ChatItem(
                            onClick = {
                                navController.navigate(
                                    Screens.Chat.withExistenceData(
                                    chatExist = true,
                                    chatName = it.chatName,
                                    chatUrl = it.iconUrl,
                                    companionUid = "companionUid",
                                    chatUid = it.chatId
                                ))
                            },
                            onLongClick = {
                                showBottomSheet.value= true
                            },
                            chatName = it.chatName,
                            iconPainter = rememberAsyncImagePainter(model =  it.iconUrl),
                            isOnline = it.isOnline,
                            lastMessageTimeString = it.lastMessageTimeString,
                            chatContent = {
                                when(it.typingStatus){
                                    TypingStatus.None ->{
                                        it.contentMessage?.let {
                                            contentMessage->
                                            Text(
                                                text =contentMessage.contentSender+contentMessage.message.text,
                                                fontSize = 15.sp,
                                                fontWeight = FontWeight.Normal,
                                                color= Color.Black.copy(alpha = 0.7f)
                                            )
                                        }

                                    }
                                    is TypingStatus.Typing -> {
                                        Text(
                                            text = it.typingStatus.whoTyping,
                                            fontSize = 15.sp,
                                            fontWeight = FontWeight.Normal,
                                            color= Color.Black.copy(alpha = 0.7f)
                                        )
                                    }
                                }
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