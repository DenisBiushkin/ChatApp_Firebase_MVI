package com.example.unmei.presentation.create_group_feature.utils

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.unmei.R
import com.example.unmei.domain.usecase.user.GetUsersExByFullName


@Composable
@Preview(showBackground = true)
fun showGroupUserItemList(

){
    LazyColumn (
        Modifier.fillMaxSize()
    ){
        items(10){

            if(it%2==0){
                GroupUserItemListWithIndicator(
                    painter= painterResource(id = R.drawable.erishkagel),
                    fullName="Marcile Donato",
                    lastSeen="был(a) 3 дня назад",
                    isSelected = true,
                    onClick = {}
                )
            }else{
                GroupUserItemList(
                    painter= painterResource(id = R.drawable.erishkagel),
                    fullName="Marcile Donato",
                    lastSeen="был(a) 3 дня назад",
                    onClick = {}
                )
            }
        }

    }
}

@Composable
fun GroupUserItemList(
    painter: Painter,
    fullName: String,
    lastSeen:String,
    clickEnable:Boolean=true,
    onClick:()->Unit
){
    Row(
        modifier = Modifier
            .height(60.dp)
            .fillMaxWidth()
            .clickable(
                enabled = clickEnable,
                onClick = onClick
            )
        ,
        verticalAlignment = Alignment.CenterVertically
    ) {
       Spacer(modifier = Modifier.width(10.dp))
       Image(
           modifier = Modifier
               .size(50.dp)
               .clip(CircleShape),
           painter = painter,
           contentDescription = "",
           contentScale = ContentScale.Fit
       )
        Spacer(modifier = Modifier.width(10.dp))
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Text(
                text = fullName,
                fontSize = 17.sp,
                fontWeight = FontWeight.Medium
            )
            Text(
                text =lastSeen,
                fontSize = 17.sp,
                fontWeight = FontWeight.Normal,
                color = Color.Black.copy(alpha = 0.5f)
            )
        }
    }
}
