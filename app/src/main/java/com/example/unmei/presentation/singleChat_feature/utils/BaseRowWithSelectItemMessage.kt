package com.example.unmei.presentation.singleChat_feature.utils

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.unmei.presentation.chat_list_feature.model.MessageStatus
import com.example.unmei.presentation.util.ui.theme.primaryMessageColor
import com.example.unmei.presentation.util.ui.theme.primaryOwnMessageColor
import com.example.unmei.presentation.chat_list_feature.util.MessageIconStatus
import com.example.unmei.presentation.singleChat_feature.model.MessageListItemUI
import com.example.unmei.presentation.singleChat_feature.model.MessageType
import com.example.unmei.presentation.util.ui.theme.chatBacgroundColor
import com.example.unmei.presentation.util.ui.theme.colorApp
import java.time.LocalDateTime

@Preview(showBackground = true)
@Composable
fun showBubble(){


    val testItem=MessageListItemUI(
        fullName = "name",
        timestamp = LocalDateTime.now(),
        timeString = "0:18",
        isOwn = false,
        status = MessageStatus.Send,
        isChanged = false,
        type = MessageType.Text,
        text = "Привет",
        visvilityOptins = true
    )

      Column (
          modifier = Modifier
              .fillMaxSize()
              .background(color = chatBacgroundColor)
            //  .padding(top = 40.dp)
      ){
//          BaseRowWithSelectItemMessage(
//              itemUI = testItem,
//              optionVisibility = true,
//
//              onClickLine = {
//
//              },
//              onLongClickLine = {
//
//              },
//              selected = true
//          )
//          Spacer(modifier = Modifier.height(8.dp))
//          BaseRowWithSelectItemMessage(itemUI = testItem.copy(
//           text = "Длинное сообщение",
//              isOwn = true
//          ), onClickLine = {
//
//          },
//              onLongClickLine = {
//
//              },
//              selected = true,
//              optionVisibility = true
//          )
      }


}
@Composable
fun TimeMessage(
    time: String = "20 Января"
){
    Row(
        modifier = Modifier
            .fillMaxWidth()
        , horizontalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .wrapContentSize()
                .clip(CircleShape)
                .background(

                    color = Color.Gray.copy(alpha = 0.2f)
                )
        ){
            Text(
                modifier = Modifier
                    .padding(
                        horizontal = 8.dp,
                    )
                ,
                text = time,
                fontSize = 15.sp,
                color = Color.Black
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BaseRowWithSelectItemMessage(
    modifier: Modifier = Modifier,
    onClickLine:() ->Unit,
    onLongClickLine:() ->Unit,
    optionVisibility:Boolean,
    selected:Boolean,
    content: @Composable() (RowScope.()->Unit)
){
     Row(
         verticalAlignment = Alignment.Bottom,
         modifier = Modifier
             .combinedClickable(
                 onClick = onClickLine,
                 onLongClick = onLongClickLine
             )
     ){
         //в целом открытие способности выделения сообщения
         AnimatedVisibility(

             visible = optionVisibility
         ) {
             Box(
                 modifier = Modifier
                     .padding(start = 3.dp, end = 5.dp)
                     .size(25.dp)
                     .background(
                         shape = CircleShape,
                         color = Color.Transparent
                     )
                     .border(
                         width = 2.dp,
                         color = if(selected) Color.White else colorApp,
                         shape = CircleShape,
                     )
                 ,
                 contentAlignment = Alignment.Center
             ){
                 Row (
                     horizontalArrangement = Arrangement.Center,
                     verticalAlignment = Alignment.CenterVertically
                 ){
                     //сообщение выбрано
                     AnimatedVisibility(
                         visible =selected
                     ) {

                         Icon(
                             modifier = Modifier
                                 .size(22.dp)
                                 .background(
                                     shape = CircleShape,
                                     color = colorApp
                                 )
                             ,
                             imageVector = Icons.Default.Check,
                             contentDescription ="",
                             tint = Color.White
                         )
                     }
                 }

             }
         }
         content()
     }
}








fun CustomBubbleMessageShape(
    cornerRadiusDp : Dp =30.dp,
    trackRadiusDp:Dp = 8.dp,//радиус завитушки слева
    position:Boolean =false,
    density: Density
): Shape = GenericShape { size, layoutDirection ->
    // val cornerRadius = 30f
    // Получаем текущую плотность экрана
    // Преобразуем dp в пиксели
    val cornerRadius = with(density) { cornerRadiusDp.toPx() }// в пикселях
    //радиус завитушки слева
    val track = with(density){ trackRadiusDp.toPx() }
    if(position) {
        //свои сообщения
        val path = Path().apply {
            val path1 = Path().apply {
                //  moveTo(0f, cornerRadius)
                moveTo(cornerRadius, size.height)
                arcTo(
                    rect = Rect(
                        left = 0f,
                        bottom = size.height,
                        right = cornerRadius * 2,
                        top = size.height - cornerRadius * 2
                    ),
                    startAngleDegrees = 90f,
                    sweepAngleDegrees = 90f,
                    false
                )
                lineTo(0f, cornerRadius)
                arcTo(
                    //прямоугольник
                    //дуга-круг будут в этом триугольнике
                    rect = Rect(
                        0f,
                        0f,
                        cornerRadius * 2,
                        cornerRadius * 2,
                    ),
                    startAngleDegrees = 180f,//угол начала отсчет от 3 часов по часов стрелке
                    sweepAngleDegrees = 90f,//на сколько повернуть
                    forceMoveTo = false
                )

                lineTo(size.width - cornerRadius - track, 0f)
                arcTo(
                    rect = Rect(
                        left = size.width - cornerRadius * 2 - track,
                        top = 0f,
                        bottom = cornerRadius * 2,
                        right = size.width - track
                    ),
                    startAngleDegrees = 270f,
                    sweepAngleDegrees = 90f,
                    forceMoveTo = false
                )
                lineTo(size.width - track, size.height - track)


                // close()
            }
            addPath(path1)
            val path2 = Path().apply {
                moveTo(cornerRadius, size.height)
                // lineTo(0f,size.height)
                lineTo(size.width, size.height)
                arcTo(
                    rect = Rect(
                        top = size.height - track * 2,
                        left = size.width - track,
                        bottom = size.height,
                        right = size.width + track
                    ),
                    sweepAngleDegrees = 90f,
                    startAngleDegrees = 90f,
                    forceMoveTo = false
                )
                // lineTo(0f,0f)
            }
            addPath(path2)
        }
        //фигура состоит из двух путей
        addPath(path)
        close()
    }else{
        // сообщения других
        moveTo(size.width-cornerRadius,size.height)
        lineTo(0f,size.height)
        arcTo(
            rect= Rect(
                left = -track,
                top= size.height-track*2,
                bottom = size.height,
                right = track
            ),
            startAngleDegrees = 90f,
            sweepAngleDegrees = -90f,
            forceMoveTo =false
        )
        lineTo(track,cornerRadius)
        arcTo(
            rect= Rect(
                left = track,
                top= 0f,
                bottom = cornerRadius*2,
                right = cornerRadius*2+track
            ),
            startAngleDegrees = 180f,
            sweepAngleDegrees = 90f,
            forceMoveTo =false
        )
        lineTo(size.width-cornerRadius,0f)
        arcTo(
            rect= Rect(
                left = size.width-cornerRadius*2,
                top= 0f,
                bottom = cornerRadius*2,
                right = size.width
            ),
            startAngleDegrees = 270f,
            sweepAngleDegrees = 90f,
            forceMoveTo =false
        )
        lineTo(size.width,size.height-cornerRadius)
        arcTo(
            rect= Rect(
                left = size.width-cornerRadius*2,
                top= size.height-cornerRadius*2,
                bottom = size.height,
                right = size.width
            ),
            startAngleDegrees = 0f,
            sweepAngleDegrees = 90f,
            forceMoveTo =false
        )
        close()
    }
}
