package com.example.unmei.presentation.singleChat_feature.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarMessageActions(
    isVisible:Boolean,
    onOffClickOptions:()->Unit,
    onDeleteClickMessages:()->Unit
){
    AnimatedVisibility(
        visible = isVisible
        , enter = fadeIn(),
        exit = fadeOut()
    ) {
        TopAppBar(
            title = { /*TODO*/ },
            modifier = Modifier.fillMaxWidth(),
            navigationIcon = {
                IconButton(
                    modifier = Modifier
                        .padding(start = 5.dp)
                    , onClick = onOffClickOptions
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        tint = Color.Black,
                        contentDescription = null
                    )
                }
            },
            actions = {
                IconButton(
                    modifier = Modifier
                        .padding(start = 5.dp)
                    ,
                    onClick = onDeleteClickMessages
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        tint = Color.Black,
                        contentDescription = null
                    )
                }
            }
        )
    }

}