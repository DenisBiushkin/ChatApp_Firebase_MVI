package com.example.unmei.presentation.singleChat_feature.components

import android.Manifest
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.IntOffset
import coil.compose.rememberAsyncImagePainter
import com.example.unmei.domain.model.AttachmentDraft
import com.example.unmei.presentation.singleChat_feature.ContentResolverClient
import com.example.unmei.presentation.singleChat_feature.utils.BottomButtonSelectMedia
import com.example.unmei.presentation.singleChat_feature.utils.CircleCountIndicatorSelectedItem
import com.example.unmei.presentation.singleChat_feature.utils.LoadingContentProgressIndicator
import com.example.unmei.util.ConstansDev.TAG
import kotlin.math.roundToInt





@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun ConversationModalBottom(
    bottomSheetState: SheetState,
    visibility :Boolean = false,
    onDismissRequest: ()->Unit,
    onSelectedMedia:(List<AttachmentDraft>)->Unit
){
//Сори бро, но редачить времени уже нет
    val screenSettings= LocalConfiguration.current
    val sizeOneItemDp=screenSettings.screenWidthDp.dp /3

    val selectedImages = remember { mutableStateOf<List<Uri>>(emptyList()) }
    val listImageUris = remember { mutableStateOf<List<Uri>>(emptyList()) }
    val isLoadingImages = remember { mutableStateOf<Boolean>(false) }
    val context = LocalContext.current
    if (visibility) {

        ModalBottomSheet(
            modifier = Modifier
                .fillMaxWidth()
                .height(500.dp)
            ,
            sheetState = bottomSheetState,
            onDismissRequest = onDismissRequest,
            shape = RoundedCornerShape(
                topStart = 10.dp, topEnd = 10.dp
            ),
            dragHandle = {}
            // windowInsets = WindowInsets.,
        ) {
            Scaffold(
                modifier = Modifier
                    .fillMaxSize()
                ,
                topBar = {
                    Row (
                        modifier = Modifier
                            .height(60.dp)
                            .fillMaxWidth()
                            .background(Color.Red)
                    ){
                    }
                },

                bottomBar = {
                    BottomButtonSelectMedia(
                        mediaSelected = selectedImages.value.size !=0,
                        countSelectedMedia = selectedImages.value.size,
                        onClick ={
                            if (selectedImages.value.size !=0){
                                val selectedAttachmentDraft = selectedImages.value.map{
                                    AttachmentDraft(
                                        uri = it,
                                        mimeType = "image/"
                                    )
                                }
                                onSelectedMedia( selectedAttachmentDraft)
                                selectedImages.value = emptyList()
                            }else{
                                onDismissRequest()
                            }
                        }
                    )
                }
            ) {
                paddingValues ->
                // Log.d(TAG,bottomSheetState.requireOffset().dp.toString())

                LaunchedEffect(key1 = isLoadingImages) {
                    listImageUris.value= ContentResolverClient(context).getAllImagesUri().take(20)
                    isLoadingImages.value= false
                }

                if(isLoadingImages.value){
                    LoadingContentProgressIndicator(
                        // modifier = Modifier.padding(paddingValues),
                        visibility = true
                    )
                }else{

                    //Сетка только для выбора фотографий
                    LazyVerticalGrid(
                        modifier = Modifier.padding(paddingValues)
                        ,columns = GridCells.Fixed(3),
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                    ) {
                        items(listImageUris.value){
                            Box(
                                modifier = Modifier
                                    .size(sizeOneItemDp)
                                    .padding(top = 4.dp)
                            ){
                                Image(
                                    modifier = Modifier
                                        .clip(RectangleShape)
                                        .size(sizeOneItemDp)
                                    ,
                                    painter = rememberAsyncImagePainter(it),
                                    contentScale = ContentScale.Crop,
                                    contentDescription = "Picture",
                                )
                                //Кружочек выбора
                                Row (
                                    modifier = Modifier.fillMaxSize(),
                                    horizontalArrangement = Arrangement.End,
                                    verticalAlignment = Alignment.Top
                                ){
                                    CircleCountIndicatorSelectedItem(
                                        modifier = Modifier
                                            .padding(
                                                top = 4.dp,
                                                end = 4.dp
                                            )
                                        ,
                                        isSelected= selectedImages.value.contains(it),
                                        count = selectedImages.value.indexOf(it)+1,
                                        onClickRound = {
                                            if(selectedImages.value.contains(it)){
                                                selectedImages.value= selectedImages.value.minus(it)
                                            }else{
                                                selectedImages.value= selectedImages.value.plus(it)
                                            }
                                        }
                                    )
                                }
                            }
                        }
                    }
                }



            }
        }


    }
}
