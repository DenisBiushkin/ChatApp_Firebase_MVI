package com.example.unmei.presentation.messanger_feature.components

import androidx.compose.animation.core.VisibilityThreshold
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.foundation.layout.wrapContentWidth
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
import com.example.unmei.presentation.messanger_feature.model.MessageStatus
import com.example.unmei.presentation.messanger_feature.util.MessageIconStatus

@Preview(showBackground = true)
@Composable
fun showChatItem(){


    Column (
        modifier = Modifier.fillMaxSize()
    ){
        ChatItem()
    }

}


@Composable
fun ChatItem(

){

   val height= 70.dp
    val isOnline = true
    Row (
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = height, max = height)
    ){

        Box(
          modifier = Modifier
              .fillMaxHeight()
              .fillMaxWidth(0.19f)//19%
          , contentAlignment =Alignment.Center
        ){
              BoxWithImageUser(
                  imageRes = R.drawable.test_user,
                  height =height,
                  isOnline = true
              )
        }
        val colorUnderLine =Color.Black.copy(alpha = 0.7f)
        val strokewidth = 1f

        BoxWithUnderline(
            modifier = Modifier,
            colorUnderLine =colorUnderLine,
            strokewidth
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
                           top =startPedding,
                           start = startPedding,
                           end = 6.dp,
                       )
                     //  .background(Color.Yellow)
                     ,horizontalArrangement = Arrangement.SpaceBetween,
                     verticalAlignment = Alignment.CenterVertically
                 ){
                     //86% это весь
                     //верхняя строчка
                     Row(
                         modifier = Modifier
                             .wrapContentSize()
                             .fillMaxWidth(0.70f)
                            // .background(Color.Red)
                         , verticalAlignment = Alignment.CenterVertically
                     ) {
                         Text(
                             modifier = Modifier
                                 .widthIn(max= 200.dp)
                             ,
                             text = "Марсиль Донато",
                             fontWeight = FontWeight.Medium,
                             maxLines = 1,
                             fontSize = 17.sp,
                             overflow = TextOverflow.Ellipsis
                         )
                         Spacer(modifier = Modifier.width(5.dp))
                         Icon(
                             modifier = Modifier.size(20.dp),
                             imageVector =  ImageVector.vectorResource(id = R.drawable.volume_off_24px),
                             contentDescription =null,
                             tint = Color.Gray.copy(alpha = 0.5f)
                         )
                     }
                     Row (
                         verticalAlignment = Alignment.CenterVertically
                     ){
                         MessageIconStatus(
                             status = MessageStatus.Readed,
                             sizeIcon = 25.dp,
                             colorIcon = Color.Green
                         )
                         Spacer(modifier = Modifier.width(4.dp))
                         Text(
                             text = "20 Янв",
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
                             top =startPedding,
                             start = startPedding,
                             end = 6.dp,
                         )
                     ,
                     horizontalArrangement = Arrangement.SpaceBetween
                 ){
                   Text(
                       text = "Вы: Привет, как твои дела?",
                       fontSize = 15.sp,
                       fontWeight = FontWeight.Normal,
                       color= Color.Black.copy(alpha = 0.7f)
                   )
                 }
             }
            //часть под содержимое то что осталось от 22% для фотки
//            Row(
//                modifier=Modifier.fillMaxSize()
//            ) {
//                //для текста и тд
//                Box(
//                    modifier = Modifier
//                        .fillMaxHeight()
//                        .fillMaxWidth(0.86f)
//                        .background(Color.Green)
//                ){
//
//                }
//                //для иконок справа
//                Box(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .fillMaxHeight()
//                        .background(Color.Red)
//                ){
//
//                }
//            }
        }
    }
}
@Composable
fun UpDoubleLine(
    modifier:Modifier=Modifier,
){

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
    imageRes: Int,
    isOnline:Boolean= false,
    height: Dp,
){
    Box(
        modifier = Modifier
            .size(height - 10.dp)
            .drawWithCache {
                val width = size.width
                val height = size.height
                val whiteCircleRadius = 8.dp.toPx()
                val greenCircleRadius = 5.dp.toPx()
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
            ,painter = painterResource(id = imageRes),
            contentDescription = ""
        )
    }
}