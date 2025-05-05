package com.example.unmei.presentation.singleChat_feature.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.animation.with
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.unmei.presentation.singleChat_feature.model.AttachmentOption
import com.example.unmei.presentation.util.ui.theme.colorApp


@Composable
fun AttachmentOptionRow(
    mediaSelected:Boolean = false,
    countSelectedMedia:Int,
    options: List<AttachmentOption>,
    onOptionClick: (AttachmentOption) -> Unit,
    onSendClick: () -> Unit,
) {
    val iconSize = 32.dp
    val textStyle = MaterialTheme.typography.labelSmall.copy(fontSize = 12.sp)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Color.White,
                shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
            )
            .padding(all = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        LazyRow(
            modifier = Modifier.fillMaxWidth(0.8f),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            items(options){
                option->
                Column(
                    modifier = Modifier
                        .clickable { onOptionClick(option) }
                        .padding(horizontal = 6.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = option.icon,
                        contentDescription = option.label,
                        modifier = Modifier.size(iconSize),
                        tint = if (option.selected) MaterialTheme.colorScheme.primary else Color.Gray
                    )
                    Text(
                        text = option.label,
                        style = textStyle,
                        color = if (option.selected) MaterialTheme.colorScheme.primary else Color.Gray
                    )
                }
            }

        }
        TopButtonSelectMedia(
            sizeBlock=70.dp,
            onClick = onSendClick,
            mediaSelected=mediaSelected,
            countSelectedMedia=   countSelectedMedia,
        )
    }
}


@Preview(showBackground = true)
@Composable
fun showSpecButton(){
    Column(
        Modifier.fillMaxSize()
    ) {
        TopButtonSelectMedia(
            modifier = Modifier.width(70.dp),
            mediaSelected = false,
            countSelectedMedia = 0,
            onClick = {

            }

        )
        Spacer(modifier = Modifier.height(10.dp))
        TopButtonSelectMedia(
            modifier = Modifier.width(70.dp),
            mediaSelected = true,
            countSelectedMedia = 8,
            onClick = {

            }

        )
    }
}
@OptIn(ExperimentalAnimationApi::class)
@Composable
fun TopButtonSelectMedia(
    modifier: Modifier = Modifier,
    sizeBlock:Dp=70.dp,
    mediaSelected: Boolean = false,
    countSelectedMedia: Int,
    onClick: () -> Unit,
) {
    Box(
        modifier = modifier
            .size(sizeBlock)
            // .background(Color.Red)
            .clickable(onClick = onClick)
            .padding(all = 4.dp)
        ,
        contentAlignment = Alignment.BottomStart
//        horizontalArrangement = Arrangement.Center,
//        verticalAlignment = Alignment.CenterVertically
    ) {
            val iconSize= 45.dp
            val shape =RoundedCornerShape(10.dp)
            val closeColorFone =Color(0xFFE0F2F7)
            val closeColorBorder =Color(0xFFB0E2FF)
            val closeColorIcon=Color(0xFF424242)

        val acceptColorFone =Color(0xFFE8F5E9)
        val acceptColorBorder =Color(0xFFA5D6A7)
        val acceptColorIcon=Color(0xFF388E3C)

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        color =
                        if(!mediaSelected)
                            closeColorFone
                        else
                            acceptColorFone,
                        shape =  shape
                    )
                    .border(
                        width = 2.dp,
                        color =if(!mediaSelected)closeColorBorder else acceptColorBorder,
                        shape =  shape
                    )
                ,
                contentAlignment = Alignment.TopEnd
            ) {

                if(mediaSelected){
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Готово",
                        tint = acceptColorIcon,
                        modifier = Modifier
                            .size(iconSize)
                            .align(Alignment.Center)
                    )
                    Box(
                        modifier = Modifier
                            .size(20.dp)
                            .background(Color.Transparent, shape = CircleShape)
                            .border(2.dp,acceptColorBorder , shape = CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "$countSelectedMedia",
                            color = acceptColorIcon,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }

                }else{
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Отмена",
                        tint = closeColorIcon,
                        modifier =Modifier
                            .size(iconSize)
                            .align(Alignment.Center)
                    )
                }


            }

    }
}