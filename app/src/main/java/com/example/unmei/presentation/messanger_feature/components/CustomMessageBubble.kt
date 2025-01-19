package com.example.unmei.presentation.messanger_feature.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.GenericShape
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.unmei.presentation.util.ui.theme.primaryMessageColor
import com.example.unmei.presentation.util.ui.theme.primaryOwnMessageColor
import com.example.unmei.presentation.messanger_feature.model.MessageStatus
import com.example.unmei.presentation.messanger_feature.model.TypeMessage
import com.example.unmei.presentation.messanger_feature.util.MessageIconStatus
import com.google.protobuf.Internal.BooleanList

@Preview(showBackground = true)
@Composable
fun showBubble(){
    Box(modifier = Modifier.fillMaxSize()){
      Column (
          modifier = Modifier.padding(top=40.dp)
      ){
          CustomMessageBubble(true)
      }
    }

}

@Composable
fun CustomMessageBubble(
    isOwnMassage: Boolean
){
    Row (
        modifier = Modifier.fillMaxSize(),
        horizontalArrangement = if(isOwnMassage) Arrangement.End else Arrangement.Start
    ){
      ChatBubbleWithPattern(isOwn = isOwnMassage) {
          MessageContenet(
              text = "Привет, это очень длинный текст сообщения "
          )
      }
    }
}

@Composable
fun MessageContenet(
    text:String ="",
    time:String ="18:30",
    isChanged:Boolean = false,
    isOwn:Boolean= true,
    status: MessageStatus = MessageStatus.Error,
    typeMessage: TypeMessage = TypeMessage.Text
){
    val textWidth= 15.sp
    val fontSizeStatus= 12.sp
    val horizontalPadding= 4.dp
    when(typeMessage){
        TypeMessage.Audio -> {

        }
        TypeMessage.Photo -> {

        }
        TypeMessage.Text -> {

        }
    }
    Box(
        modifier = Modifier.wrapContentSize()
            .padding(
                start = horizontalPadding,
                end = horizontalPadding,
                top = horizontalPadding,
                bottom = 0.dp
            ),
        contentAlignment = Alignment.Center
    ){
        Text(
            text =text,
            fontSize = textWidth
        )
    }
    Row (
        modifier = Modifier.fillMaxWidth()
        , horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.CenterVertically

    ) {
        Text(text = "ред.", fontSize = fontSizeStatus)
        Spacer(modifier = Modifier.width(4.dp))
        if(isOwn) Text(text =time , fontSize = fontSizeStatus)
        Spacer(modifier = Modifier.width(4.dp))
        MessageIconStatus(
            status= status,
            sizeIcon = 18.dp
        )
    }
}

@Composable
fun ChatBubbleWithPattern(
    modifier: Modifier = Modifier,
    isOwn:Boolean= false,
    content: @Composable (ColumnScope.() -> Unit)
){
    val otherPadding = 5.dp
    val compensatePadding=15.dp
    val screenSettings=LocalConfiguration.current
    val maxWidth=screenSettings.screenWidthDp.dp *0.8f

    val modifierLeftMassage= modifier
        .padding(
            start = compensatePadding,
            top = otherPadding,
            bottom = otherPadding,
            end = otherPadding
        )
        .widthIn(max = maxWidth)
    val modifierRightMassage = modifier
        .padding(
            start = otherPadding,
            top = otherPadding,
            bottom = otherPadding,
            end = compensatePadding
        )
        .widthIn(max = maxWidth)
    val density = LocalDensity.current
    val cornerRadius = 15.dp
    val leftMessageShape = remember {
        CustomBubbleMessageShape(density=density, cornerRadiusDp =cornerRadius)
    }
    val rightMessageShape = remember {
        CustomBubbleMessageShape(density=density,position = true, cornerRadiusDp = cornerRadius)
    }
    val colorMessageBox= Color.Magenta


    if(isOwn){
        Box(
            modifier = modifier
                .clip(shape = rightMessageShape)
                .background(primaryOwnMessageColor)
        ){
            Column(
                modifier =  modifierRightMassage
            ) {
                content()
            }
        }
    }else{
        Box(
            modifier = modifier
                .clip(shape = leftMessageShape)
                .background(color = primaryMessageColor)
        ){
            Column(
                modifier =modifierLeftMassage
            ) {
                content()
            }
        }
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
                top= track*2,
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
