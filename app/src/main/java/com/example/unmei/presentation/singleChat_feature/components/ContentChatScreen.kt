package com.example.unmei.presentation.singleChat_feature.components
import android.nfc.Tag
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.unmei.presentation.groupChat_feature.utils.TimeDivider
import com.example.unmei.presentation.singleChat_feature.model.MessageListItemUI
import com.example.unmei.presentation.singleChat_feature.model.MessageType
import com.example.unmei.presentation.singleChat_feature.utils.BaseRowWithSelectItemMessage
import com.example.unmei.presentation.singleChat_feature.utils.ChatBubbleOnlyImageMessage
import com.example.unmei.presentation.singleChat_feature.utils.ChatBubbleWithPattern
import com.example.unmei.presentation.singleChat_feature.utils.LoadingCircleProgressNewMessages
import com.example.unmei.presentation.singleChat_feature.utils.MessageContent
import com.example.unmei.presentation.util.ui.theme.chatBacgroundColor
import com.example.unmei.util.ConstansDev.TAG
import java.time.LocalDate


@RequiresApi(35)
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ContentChatScreen(
    modifier: Modifier = Modifier,
    lazyState: LazyListState,
    isLoadingOldMessages:Boolean,
    listMessage:List<MessageListItemUI>,
    selectedMessages: Map<String,Boolean>,
    optionsVisibility:Boolean,
    onClickMessageLine:(String)->Unit,
    onLongClickMessageLine:(String)->Unit,
    groupedListMessage:Map<LocalDate,List<MessageListItemUI>>,
) {
    LazyColumn(
        state = lazyState,
        modifier = modifier
            .fillMaxSize()
            .background(color = chatBacgroundColor)
            .padding(start = 4.dp, end = 4.dp)
        , reverseLayout = true,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {

        item{
          LoadingCircleProgressNewMessages(isLoadingOldMessages)
        }
        groupedListMessage.forEach { groupedList ->

            items(groupedList.value) { message ->
                BaseRowWithSelectItemMessage(
                    optionVisibility = optionsVisibility,
                    onClickLine = {
                        onClickMessageLine(message.messageId)
                    },
                    onLongClickLine = { onLongClickMessageLine(message.messageId) },
                    selected = selectedMessages[message.messageId] == true
                ) {
                    //Смотрим тип сообщения

                    when (message.type) {
                        is MessageType.OnlyImage -> {
                            Log.d(TAG, "MessageType.OnlyImage")
                            ChatBubbleOnlyImageMessage(item = message)
                        }

                        is MessageType.Text -> {
                            Log.d(TAG, "MessageType.Text ")
                            ChatBubbleWithPattern(
                                modifier = Modifier,
                                isOwn = message.isOwn,
                            ) {
                                MessageContent(
                                    data = message
                                )
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
            //снизу т.к reverseLayout
            item(groupedList.key) {
                TimeDivider(timeText=groupedList.key.toString())
            }
        }

//        items(listMessage) {
//            message ->
//            BaseRowWithSelectItemMessage(
//                optionVisibility =optionsVisibility,
//                onClickLine = {
//                        onClickMessageLine(message.messageId)
//                },
//                onLongClickLine = { onLongClickMessageLine(message.messageId) },
//                selected = selectedMessages[message.messageId] == true
//            ) {
//                //Смотрим тип сообщения
//
//                when(message.type){
//                    is MessageType.OnlyImage -> {
//                        Log.d(TAG,"MessageType.OnlyImage")
//                        ChatBubbleOnlyImageMessage(item = message)
//                    }
//                    is  MessageType.Text -> {
//                        Log.d(TAG,"MessageType.Text ")
//                        ChatBubbleWithPattern(
//                            modifier= Modifier,
//                            isOwn = message.isOwn,
//                            ) {
//                            MessageContent(
//                                data = message
//                            )
//                        }
//                    }
//                }
//            }
//            Spacer(modifier = Modifier.height(8.dp))
//        }
    }
//    if(!state.value.selectedUrisForRequest.isEmpty()){
//        viewModel.testFun()
//    }
}