package com.example.unmei.presentation.chat_list_feature.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import  com.example.unmei.R
import com.example.unmei.presentation.chat_list_feature.model.MessageStatus
import com.example.unmei.presentation.chat_list_feature.model.NotificationMessageStatus
import com.example.unmei.presentation.chat_list_feature.util.MessageIconStatus
import com.example.unmei.presentation.chat_list_feature.model.ChatListItemUI
import java.time.Instant
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

@Preview(showBackground = true)
@Composable
fun showChatItem(){


    Column (
        modifier = Modifier.fillMaxSize()
    ){
        val item = ChatListItemUI(
            messageStatus=MessageStatus.Send,
            notificationMessageStatus = NotificationMessageStatus.On,
            isOnline= true,
            fullName= "Marcile Donato",
            painterUser= painterResource(id =  R.drawable.test_user),
            messageText= "Вы: Привет, как твои дела?",
            //пока что String
            timeStamp= 1737392296,
            groupUid = ""
        )
        ChatItem(
            onClick = {

            },
            onLongClick = {

            },
            chatName = item.fullName,
            iconPainter = item.painterUser,
            isOnline = item.isOnline,
            chatContent = {

            }

        )
    }

}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ChatItem(
    height: Dp = 70.dp,
    colorUnderLine:Color =Color.Black.copy(alpha = 0.7f),
    underLineWidth:Float =1f,
    onClick:()->Unit,
    onLongClick:()->Unit,
    notificationMessageStatus:NotificationMessageStatus=NotificationMessageStatus.On,
    lastMessageTimeString: String = "",
    chatContent: @Composable() (RowScope.()->Unit),
    chatName:String = "",
    iconPainter: Painter,
    isOnline: Boolean = false,
    messageStatus:MessageStatus = MessageStatus.Send
){
    Row (
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = height, max = height)
            .combinedClickable(
                onClick = {
                    onClick()
                },
                onLongClick = {
                    onLongClick()
                }
            )
    ){
        Box(
          modifier = Modifier
              .fillMaxHeight()
              .fillMaxWidth(0.19f)//19%
          , contentAlignment =Alignment.Center
        ){
              BoxWithImageUser(
                  painterUser =iconPainter,
                  height =height,
                  isOnline =isOnline
              )
        }
        BoxWithUnderline(
            modifier = Modifier,
            colorUnderLine =colorUnderLine,
           underLineWidth= underLineWidth
        ) {
             Column(
                 modifier = Modifier
                     .fillMaxSize()
             ) {
                 //верхняя часть
                 val startPedding= 2.dp
                 Row (
                   modifier = Modifier
                       .fillMaxWidth()
                       .fillMaxHeight(0.5f)
                       .padding(
                           top = startPedding,
                           start = startPedding,
                           end = 6.dp,
                       )
                     ,horizontalArrangement = Arrangement.SpaceBetween,
                     verticalAlignment = Alignment.CenterVertically
                 ){
                     //86% это весь
                     //верхняя строчка
                     Row(
                         modifier = Modifier
                             .wrapContentSize()
                             .fillMaxWidth(0.70f)
                         , verticalAlignment = Alignment.CenterVertically
                     ) {
                         Text(
                             modifier = Modifier
                                 //отредактировать
                                 .widthIn(max= 200.dp)
                             ,
                             text=chatName,
                             fontWeight = FontWeight.Medium,
                             maxLines = 1,
                             fontSize = 17.sp,
                             overflow = TextOverflow.Ellipsis
                         )
                         Spacer(modifier = Modifier.width(5.dp))

//                         when(notificationMessageStatus){
//                             NotificationMessageStatus.Off ->{
//                                 Icon(
//                                     modifier = Modifier.size(20.dp),
//                                     imageVector =  ImageVector.vectorResource(id = R.drawable.volume_off_24px),
//                                     contentDescription =null,
//                                     tint = Color.Gray.copy(alpha = 0.5f)
//                                 )
//                             }
//                             NotificationMessageStatus.On -> {
//
//                             }
//                         }
                     }
                     Row (
                         verticalAlignment = Alignment.CenterVertically
                     ){
                         MessageIconStatus(
                             status = messageStatus,
                             sizeIcon = 25.dp,
                             colorIcon = Color.Green
                         )
                         Spacer(modifier = Modifier.width(4.dp))
                         Text(
                             text = lastMessageTimeString,
                             color = Color.Gray,
                             fontSize = 14.sp
                         )

                     }
                 }
                 //Нижняя часть
                 Row (
                     modifier = Modifier
                         .fillMaxWidth()
                         .fillMaxHeight()
                         .padding(
                             top = startPedding,
                             start = startPedding,
                             end = 6.dp,
                         )
                     ,
                     horizontalArrangement = Arrangement.SpaceBetween,
                     content = chatContent
                 )
//                 {
//
//                     ////&&&&\\\\\\\
//                   Text(
//                       text = item.messageText,
//                       fontSize = 15.sp,
//                       fontWeight = FontWeight.Normal,
//                       color= Color.Black.copy(alpha = 0.7f)
//                   )
//
//
//                 }
             }
        }
    }
}

@Composable
fun BoxWithUnderline(
    modifier:Modifier=Modifier,
    colorUnderLine: Color,
    underLineWidth:Float =1f,
    content: @Composable() (BoxScope.()->Unit)
){
    Box(
        modifier = modifier
            .fillMaxHeight()
            .fillMaxWidth()
            .drawWithCache {
                val width = size.width
                val height = size.height
                onDrawWithContent {
                    drawContent()
                    drawLine(
                        start = Offset(0f, height - underLineWidth),
                        end = Offset(width, height - underLineWidth),
                        color = colorUnderLine,
                        strokeWidth = underLineWidth
                    )
                }
            },
        content = content
    )
}
@Composable
fun BoxWithImageUser(
    modifier: Modifier=Modifier,
    painterUser: Painter,
    isOnline:Boolean= false,
    height: Dp,
    statusCircleRadius:Dp = 8.dp
){
    Box(
        modifier = modifier
            .size(height - 10.dp)
            .drawWithCache {
                val width = size.width
                val height = size.height
                val whiteCircleRadius = statusCircleRadius.toPx()
                val greenCircleRadius = whiteCircleRadius*0.7.toFloat()
                val centerWhite = Pair(height - whiteCircleRadius, width - whiteCircleRadius)
                onDrawWithContent {
                    drawContent()
                    if (isOnline) {
                        drawCircle(
                            color = Color.White,
                            center = Offset(centerWhite.first, centerWhite.second),
                            radius = whiteCircleRadius
                        )
                        drawCircle(
                            color = Color.Green,
                            center = Offset(centerWhite.first, centerWhite.second),
                            radius = greenCircleRadius
                        )
                    }
                }
            }
    ){
        Image(
            modifier = Modifier
                .size(height - 10.dp)
                .clip(CircleShape)
            ,painter = painterUser,
            contentDescription = ""
        )
    }
}