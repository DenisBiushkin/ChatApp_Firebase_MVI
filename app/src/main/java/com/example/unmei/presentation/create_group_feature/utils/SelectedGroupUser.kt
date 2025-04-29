package com.example.unmei.presentation.create_group_feature.utils

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.unmei.R


@Composable
@Preview(showBackground = true)
fun showSelectedGroupUser(){
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        SelectedGroupUser(
            painter = painterResource(id = R.drawable.test_user),
            name = "Marcile",
            onClickReject = {

            }
        )
    }
}
@Composable
fun SelectedGroupUser(
    modifier: Modifier=Modifier,
    painter: Painter,
    name:String,
    onClickReject:()->Unit
){
    Column(
        modifier = modifier
            .height(90.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ImageBoxWithRejectIcon(
            painter,
            onClickReject = onClickReject
        )
        Spacer(modifier = Modifier.height(5.dp))
        Text(
            text = name,
            fontWeight = FontWeight.Normal,
            fontSize = 17.sp
        )
    }
}
@Composable
fun ImageBoxWithRejectIcon(
    painter: Painter,
    onClickReject:()->Unit
){
    Box(
        modifier = Modifier.size(
            60.dp
        ),
        contentAlignment = Alignment.TopEnd
    ){
        Image(
            modifier = Modifier.clip(CircleShape),
            painter = painter,
            contentDescription = ""
        )
        IconButton(onClick = onClickReject) {
            Icon(imageVector = Icons.Default.Close, contentDescription = "")
        }

    }
}