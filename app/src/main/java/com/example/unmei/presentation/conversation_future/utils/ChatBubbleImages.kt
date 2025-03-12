package com.example.unmei.presentation.conversation_future.utils

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box

import androidx.compose.foundation.layout.Column

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import com.example.unmei.presentation.conversation_future.model.MessageListItemUI

import  com.example.unmei.R
import com.example.unmei.presentation.chat_list_feature.model.MessageStatus
import com.example.unmei.presentation.chat_list_feature.util.MessageIconStatus

@Composable
@Preview(showBackground = true)
fun showChatBubbleImages(){

    val item = MessageListItemUI(
        fullName = "",
        text = "",
        timestamp = 762768972,
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
            isOwn = true
        ) )

    }

}
@Composable
fun ChatBubbleImages(
    modifier: Modifier= Modifier,
    item: MessageListItemUI
){
    val screenSettings= LocalConfiguration.current
    val maxWidth=screenSettings.screenWidthDp.dp *0.8f

    val imageUri = item.attachments?.let {
        it.first().base64data
    }

    SimpleChatBubbleContainer(
        isOwn = item.isOwn
    ){
//        LazyVerticalGrid(
//            columns = GridCells.Adaptive(100.dp),
//            verticalArrangement = Arrangement.spacedBy(1.dp),
//            horizontalArrangement = Arrangement.spacedBy(1.dp)
//        ) {
//            items(10){
        Image(
            modifier = Modifier
                .aspectRatio(1f)
            // .clip(RoundedCornerShape(20.dp))
            ,contentScale = ContentScale.Crop,
            painter = rememberAsyncImagePainter(model = imageUri)
            ,contentDescription = ""
        )
        Row {
            StatusTimeMessageBlock(
                modifier = Modifier
                    .padding(end = 5.dp, bottom = 5.dp),
                status = item.status,
                stringTime = item.timeString,
                isChanged = item.isChanged
            )
        }

    }
}

@Composable
fun StatusTimeMessageBlock(
    modifier: Modifier = Modifier,
    colorText:Color = Color.White,
    containerColor:Color = Color.Black.copy(alpha = 0.3f),
    isChanged:Boolean = false,
    status:MessageStatus = MessageStatus.None,
    stringTime:String = ""

){
    val fontSizeStatus = 12.sp
        Box(
           modifier = modifier
               .clip(CircleShape)
               .height(20.dp)
               .background(
                   color = containerColor
               )
               .padding(
                   start = 4.dp, end = 2.dp,
                   top = 2.dp, bottom = 2.dp
               )
            ,
            contentAlignment = Alignment.Center
        ) {
            Row (
                modifier = Modifier.wrapContentWidth()
                ,
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically

            ) {
                if (isChanged)
                    Text(
                        text = "ред.",
                        fontSize = fontSizeStatus,
                        color = colorText
                    )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text =stringTime,
                    fontSize = fontSizeStatus,
                    color = colorText
                )
                Spacer(modifier = Modifier.width(4.dp))
                //убрать сравнение тут и сравнивать в viemodel Status.None

                    MessageIconStatus(
                        status=status,
                        sizeIcon = 18.dp,
                        colorIcon = colorText
                    )
            }
        }
       


}