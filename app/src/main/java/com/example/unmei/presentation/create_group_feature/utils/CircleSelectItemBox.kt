package com.example.unmei.presentation.create_group_feature.utils

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.unmei.presentation.util.ui.theme.colorApp


@Composable
@Preview(showBackground = true)
fun showCircleSelectItemBox(){
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
        ){
        CircleSelectItemBox(true)
        CircleSelectItemBox(false)

    }
}

@Composable
fun CircleSelectItemBox(
    isSelected:Boolean
){

    Box(
        modifier = Modifier
            .size(24.dp)
            .border(
                width = 1.dp,
                shape = CircleShape,
                color = Color.Black.copy(alpha = 0.5f)
            )
            .clip(CircleShape),
        contentAlignment = Alignment.Center
    ){
        AnimatedVisibility(
            visible =isSelected
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