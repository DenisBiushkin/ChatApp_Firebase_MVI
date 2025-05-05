package com.example.unmei.presentation.singleChat_feature.utils

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box

import androidx.compose.foundation.layout.Column

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.unmei.presentation.singleChat_feature.model.MessageListItemUI

import com.example.unmei.presentation.chat_list_feature.model.MessageStatus
import com.example.unmei.presentation.chat_list_feature.util.MessageIconStatus
import java.time.LocalDateTime

@Composable
@Preview(showBackground = true)
fun showChatBubbleImages(){

    val item = MessageListItemUI(
        fullName = "",
        text = "",
        timestamp = LocalDateTime.now(),
        timeString = "11:03",
        isChanged = true,
        status = MessageStatus.None
    )
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Spacer(modifier = Modifier.height(8.dp))
        ChatBubbleImages( item =item )
        Spacer(modifier = Modifier.height(8.dp))
        ChatBubbleImages( item =item.copy(
            isOwn = true,
            status = MessageStatus.Readed
        ) )

    }

}
@Deprecated("не используется")
@Composable
fun ChatBubbleImages(
    modifier: Modifier= Modifier,
    item: MessageListItemUI
){
    val screenSettings= LocalConfiguration.current
    val maxWidth=screenSettings.screenWidthDp.dp *0.8f


    SimpleChatBubbleContainer(
        isOwn = item.isOwn
    ){
        Image(
            modifier = Modifier
           //     .aspectRatio(1f)
            // .clip(RoundedCornerShape(20.dp))
            ,contentScale = ContentScale.Crop,
            painter = rememberAsyncImagePainter(model = item.attachmentsUi.values.first())
            ,contentDescription = ""
        )
        Box(
            modifier = Modifier
                .padding(end = 5.dp, bottom = 5.dp),
            contentAlignment = Alignment.Center
        ){
            StatusTimeMessageBlock(
                status = item.status,
                stringTime = item.timeString,
                isChanged = item.isChanged
            )
        }

    }
}

