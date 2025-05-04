package com.example.unmei.presentation.chat_list_feature.util

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.unmei.presentation.chat_list_feature.components.BoxWithUnderline


@Preview(showBackground = true)
@Composable
fun showShimmerChatItem(){
    ShimmerChatItem(brush = Brush.linearGradient(
        listOf(
            Color.LightGray.copy(alpha = 0.6f),
            Color.LightGray.copy(alpha = 0.2f),
            Color.LightGray.copy(alpha = 0.6f)
        )
    ))
}

@Composable
fun ShimmerChatItem(
    height: Dp = 70.dp,
    colorUnderLine:Color =Color.Black.copy(alpha = 0.7f),
    underLineWidth:Float =1f,
    brush: Brush
){

    val textHeightDp = 15.dp
    Row (
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = height, max = height)
    ){
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(0.19f)//19%
            , contentAlignment = Alignment.Center
        ){

            //Icon
            Spacer(modifier = Modifier
                .size(height - 10.dp)
                .clip(CircleShape)
                .background(brush)
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
                        //fullname
                        Spacer(
                            modifier = Modifier
                                .width(150.dp)
                                .height(textHeightDp)
                                .clip(CircleShape)
                                .background(brush)
                        )
                        Spacer(modifier = Modifier.width(5.dp))

                    }

                    Row (
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        //блок времени отправки и статуса
                        Spacer(
                            modifier = Modifier
                                .width(25.dp)
                                .height(textHeightDp+2.dp)
                                .clip(CircleShape)
                                .background(brush)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
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
                    horizontalArrangement = Arrangement.SpaceBetween
                ){

                    Spacer(
                        modifier = Modifier
                            .width(220.dp)
                            .height(textHeightDp+3.dp)
                            .clip(CircleShape)
                            .background(brush)
                    )
                }
            }
        }
    }
}