package com.example.unmei.presentation.singleChat_feature.utils

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.unmei.R
import com.example.unmei.presentation.chat_list_feature.model.MessageStatus
import com.example.unmei.presentation.singleChat_feature.model.AttachmentTypeUI
import com.example.unmei.presentation.singleChat_feature.model.MessageListItemUI
import com.example.unmei.presentation.singleChat_feature.model.AttachmentUi
import java.time.LocalDateTime


@Preview(showBackground = true)
@Composable
fun showOnlyPhotoMessageItem(){
    Box(
        Modifier.fillMaxSize()
    ){
      LazyColumn(
          Modifier.fillMaxSize()
      ) {
          val item = MessageListItemUI(
              fullName = "",
              text = "",
              timestamp = LocalDateTime.now(),
              timeString = "11:03",
              isChanged = true,
              status = MessageStatus.None,
              attachmentsUi = mapOf(
                  "" to AttachmentUi( type = AttachmentTypeUI.IMAGE, uploadedUrl = R.drawable.tohsaka.toString())
              )
          )
          items(3){
              ChatBubbleOnlyImageMessage(item = item)
              ChatBubbleOnlyImageMessage(item = item)
          }

      }
    }

}
@Composable
fun ChatBubbleOnlyImageMessage(
    modifier: Modifier= Modifier,
    item: MessageListItemUI
){

    SimpleChatBubbleContainer(
        modifier = Modifier,
        isOwn=item.isOwn,
        cornerRadius=  15.dp,
    ){
        PhotoGrid(
            photoItems = item.attachmentsUi?.values?.toList()?: emptyList()
        )
        Box(
            modifier = Modifier
                .padding(end = 5.dp, bottom = 5.dp),
            contentAlignment = Alignment.Center
        ){
            StatusTimeMessageBlock(
                status = item.status,
                stringTime = item.timeString,
                isChanged = item.isChanged
            )
        }
    }
}

@Composable
fun PhotoGrid(photoItems: List<AttachmentUi>) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier.fillMaxWidth()
            .wrapContentHeight(),
        contentPadding = PaddingValues(8.dp),
        horizontalArrangement = Arrangement.spacedBy(2.dp),
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        items(photoItems) { photoItem ->

            if(photoItem.type!=AttachmentTypeUI.IMAGE)
                return@items
            Box(
                modifier = Modifier
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.LightGray),
                contentAlignment = Alignment.Center
            ) {
                val imageUrl_Uri=if(photoItem.isLoading) photoItem.uri else photoItem.uploadedUrl
                Image(
                    painter = rememberAsyncImagePainter(model = imageUrl_Uri),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                if (photoItem.isLoading) {
                    Box(
                        modifier = Modifier
                            .matchParentSize()
                            .background(Color.Black.copy(alpha = 0.4f)),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            progress = { photoItem.progressValue.coerceIn(0f, 1f) },
                            modifier = Modifier.size(36.dp),
                            color = Color.White,
                            strokeWidth = 4.dp,
                        )
                    }
                }
            }
        }
    }
}

