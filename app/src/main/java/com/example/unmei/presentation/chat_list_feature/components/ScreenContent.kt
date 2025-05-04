package com.example.unmei.presentation.chat_list_feature.components
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.graphics.Color

import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.unmei.domain.model.TypeRoom
import com.example.unmei.presentation.Navigation.Screens
import com.example.unmei.presentation.chat_list_feature.model.ChatListItemUiAdv
import com.example.unmei.presentation.chat_list_feature.model.TypingStatus
import com.example.unmei.presentation.chat_list_feature.viewmodel.ChatListViewModel
import com.example.unmei.presentation.singleChat_feature.model.ContentStateScreen
import com.example.unmei.presentation.util.TypingIndicator

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
        when (state.value.contentState) {

            is ContentStateScreen.Error -> {}
            ContentStateScreen.EmptyType -> EmptyChatScreen()
            ContentStateScreen.Loading -> ShimmerChatList()
            ContentStateScreen.Content -> ContentChats(
                listItems = state.value.chatListAdv,
                onClickItem = {
                    when(it.typeChat){
                        TypeRoom.PRIVATE -> {
                            navController.navigate(
                                Screens.Chat.withExistenceData(
                                    chatExist = true,
                                    chatName = it.chatName,
                                    chatUrl = it.iconUrl,
                                    companionUid = it.members.filter { it != state.value.userId }?.first() ?: ""
                                    ,chatUid = it.chatId
                                )
                            )
                        }
                        TypeRoom.PUBLIC ->{
                            navController.navigate(
                                Screens.GroupChat.withChatId(it.chatId)
                            )
                        }
                    }
                },
                onLongClickItem = {
                    showBottomSheet.value = true
                }
            )

        }
    }
    if (showBottomSheet.value) {
        ModalBottomSheet(
            dragHandle = {},
            sheetState = sheetState,
            onDismissRequest = { showBottomSheet.value = false }
        ) {
            Box(modifier = Modifier.height(200.dp)){
            }
        }
    }
}
@Composable
fun ContentChats(
    listItems:List<ChatListItemUiAdv>,
    onClickItem:(ChatListItemUiAdv)->Unit,
    onLongClickItem:(ChatListItemUiAdv)->Unit
){
    LazyColumn {
        items(listItems){
            val painter = rememberAsyncImagePainter(model = it.iconUrl)
            val state = painter.state
            ChatItem(
                messageStatus= it.messageStatus,
                onClick = { onClickItem(it) },
                onLongClick = { onLongClickItem(it) },
                chatName = it.chatName,
                //заменить потом
                iconPainter =painter,
                iconUrl = it.iconUrl,
                isOnline = it.isOnline,
                unredCount = it.unreadedCountMessage,
                lastMessageTimeString = it.lastMessageTimeString,
                chatContent = {
                    //высвечивать контент
                    when (it.typingStatus) {
                        TypingStatus.None -> {
                            it.contentMessage?.let { contentMessage ->
                                Text(
                                    text = contentMessage.contentSender + contentMessage.message.text,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Normal,
                                    color = Color.Black.copy(alpha = 0.7f)
                                )
                            }

                        }

                        is TypingStatus.Typing -> {
                            Row (
                                verticalAlignment = Alignment.CenterVertically
                            ){
                                Text(
                                    text = it.typingStatus.whoTyping,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Normal,
                                    color = Color.Blue.copy(alpha = 0.7f)
                                )
                                Spacer(modifier = Modifier.width(2.dp))
                                TypingIndicator(
                                    sizeDot = 6.dp,
                                    colorDot = Color.Blue
                                )
                            }

                        }
                    }
                }

            )
        }
    }
}
@Composable
fun ShimmerChatList(){
    LazyColumn {
        items(10) {
            AnimatedShimmerEffectChatItem()
        }
    }
}