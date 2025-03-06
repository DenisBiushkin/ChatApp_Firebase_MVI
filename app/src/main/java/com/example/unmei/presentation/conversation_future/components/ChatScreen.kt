package com.example.unmei.presentation.conversation_future.components

import android.Manifest
import android.net.Uri
import android.os.Build
import android.util.Log
import android.widget.Button
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.expandIn
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.IconToggleButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.rememberBottomSheetState
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter

import com.example.unmei.R
import com.example.unmei.presentation.conversation_future.ContentResolverClient
import com.example.unmei.presentation.conversation_future.model.ConversationEvent
import com.example.unmei.presentation.conversation_future.model.ConversationVMState
import com.example.unmei.presentation.conversation_future.utils.CustomMessageBubble
import com.example.unmei.presentation.conversation_future.utils.BottomBarChatScreen
import com.example.unmei.presentation.conversation_future.utils.LoadingCircleProgressNewMessages
import com.example.unmei.presentation.conversation_future.utils.TopBarChatScreen
import com.example.unmei.presentation.conversation_future.viewmodel.ConversationViewModel
import com.example.unmei.presentation.util.ui.theme.chatBacgroundColor
import com.example.unmei.presentation.util.ui.theme.colorApp
import com.example.unmei.util.ConstansDev.TAG
import kotlinx.coroutines.launch
import org.checkerframework.checker.units.qual.min

@Preview(showBackground = true)
@Composable
fun showChatScreen(){
   ChatScreen(
       navController = rememberNavController(),
       viewModel = hiltViewModel()
   )
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun  ChatScreen(
    navController: NavController,
    viewModel: ConversationViewModel,
) {

    val state = viewModel.state.collectAsState()
    val lazyState = rememberLazyListState()
    val bottomSheetState = rememberModalBottomSheetState()

    Scaffold(
        topBar = {
            TopBarChatScreen(
                onClickBack ={navController.popBackStack()},
                onClickProfile = {},
                iconChatPainter =  painterResource(id = R.drawable.test_user),
                titleChat = "Unknown",
                statusChat = "offline"
            )
        },
        bottomBar = {

            BottomBarChatScreen(
              viewModel=viewModel,
                lazyState = lazyState,
                vmState = state,
                onClickContent = {

                    viewModel.onEvent(ConversationEvent.OpenCloseBottomSheet)
                }
            )
        },
        //учитывает появление клавиатуры
        modifier = Modifier.imePadding()
    ) {
        paddingValues ->
        ContentChatScreen(
            modifier = Modifier.padding(paddingValues),
            state = state,
            lazyState= lazyState,
            viewModel
        )
        ConversationModalBottom(
            bottomSheetState = bottomSheetState,
            visibility = state.value.bottomSheetVisibility,
            onDismissRequest = {
              viewModel.onEvent(ConversationEvent.OpenCloseBottomSheet)
            }
        )

        
    }
}



@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun ConversationModalBottom(
    bottomSheetState: SheetState,
    visibility :Boolean = false,
    onDismissRequest: ()->Unit
){
    val screenSettings= LocalConfiguration.current
    val sizeOneItemDp=screenSettings.screenWidthDp.dp /3
    val maxHeihtBottomSheet = screenSettings.screenHeightDp.dp * 0.7f

    if (visibility) {
        ModalBottomSheet(
            modifier = Modifier.heightIn(
                min = maxHeihtBottomSheet
            ),
            dragHandle = {},
            sheetState = bottomSheetState,
            onDismissRequest = onDismissRequest,
            shape = RoundedCornerShape(
                topStart = 10.dp, topEnd = 10.dp
            )
        ) {
            Scaffold(
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
                    Row (
                        modifier = Modifier
                            .height(60.dp)
                            .fillMaxWidth()
                            .background(Color.Blue),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ){
                       Button(
                           modifier = Modifier
                               .fillMaxWidth(0.95f)
                               .height(50.dp)
                               .background(
                                   shape = RoundedCornerShape(10.dp),
                                   color = Color.Black
                               )
                           , onClick = {  }
                       ) {
                           Text(text = "Отменить")
                       }
                    }
                }
            ) {
               paddingValues ->


                val selectedImages = remember {
                    mutableStateOf<List<Uri>>(emptyList())
                }

                val listImageUris = remember {
                    mutableStateOf<List<Uri>>(emptyList())
                }
                val isLoadingImages = remember {
                    mutableStateOf<Boolean>(false)
                }
                val scope = rememberCoroutineScope()
                val context = LocalContext.current

                LaunchedEffect(key1 = isLoadingImages) {
                     listImageUris.value=ContentResolverClient(context).getAllImagesUri().take(20)
                    isLoadingImages.value= false
                }
                if(isLoadingImages.value){
                    LoadingContentProgressIndicator(true)
                }else{
                    LazyVerticalGrid(
                        modifier = Modifier
                            .padding(paddingValues)
                            .heightIn(min = sizeOneItemDp * 2.5f)
                        ,
                        columns =GridCells.Fixed(3),
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                    ) {

                        items(listImageUris.value){

                            Box(
                                modifier = Modifier
                                    .combinedClickable(
                                        onClick = {

                                        },
                                        onLongClick = {
                                        }
                                    )
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
@Composable
fun CircleCountIndicatorSelectedItem(
    modifier: Modifier = Modifier,
    isSelected:Boolean = false,
    count:Int = 1,
    onClickRound:()->Unit
){
    Box(
        modifier = modifier
            // .padding(start = 3.dp, end = 5.dp)
            .background(
                shape = CircleShape,
                color = Color.Transparent
            )
            .size(25.dp)
            .border(
                width = 2.dp,
                color = if (isSelected) Color.White else colorApp,
                shape = CircleShape,
            ).clickable (
                onClick = onClickRound
            )
        ,
        contentAlignment = Alignment.Center
    ){
      AnimatedVisibility(
          visible = isSelected,
          enter= fadeIn()+ expandIn(expandFrom = Alignment.Center),
          exit = fadeOut()
      ) {
          Box(
              modifier = Modifier
                  .size(22.dp)
                  .background(
                      shape = CircleShape,
                      color = colorApp
                  )
              ,
              contentAlignment = Alignment.Center
          ){
              Text(text = "$count", color = Color.White)
          }

      }
    }
}

@Composable
fun LoadingContentProgressIndicator(
    visibility: Boolean,

) {
   if(visibility){
     Box(
         modifier = Modifier
             .heightIn(min = 60.dp)
             .fillMaxWidth()
         , contentAlignment = Alignment.Center
     ){
         CircularProgressIndicator(
             modifier = Modifier
                 .size(30.dp),
             color = colorApp
         )
       }
   }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ContentChatScreen(
    modifier: Modifier= Modifier,
    state: State<ConversationVMState>,
    lazyState: LazyListState,
    viewModel: ConversationViewModel,

) {
    var previousIndex by remember { mutableStateOf(0) }
    val isScrolling by remember {
        //derivedStateOf оптимизирует измение,меняется только при реальном измении isScrolling а не просто recomposition
        derivedStateOf { lazyState.isScrollInProgress }
    }
    val isScrollingUp by remember {
        derivedStateOf {
            val isUp = (lazyState.firstVisibleItemIndex <previousIndex)
                 //   ||
                 //   (!lazyState.canScrollForward)//не можем больше пролестнуть вверх
            previousIndex =lazyState.firstVisibleItemIndex
            isUp
        }
    }
    val canScrollForward by remember {
        derivedStateOf {
            lazyState.canScrollForward
        }
    }
    Log.d(TAG,"canScrollForward $canScrollForward")
//    if(isScrollingUp && !state.value.loadingOldMessages){
//
//        Log.d(TAG,"Загрузка старых")
//        viewModel.onEvent(ConversationEvent.LoadingNewMessage)
//    }
    println("Идет скрол "+isScrolling)
    println("Пользователь листает вверх "+isScrollingUp)

    if (state.value.loadingScreen){
        CircularProgressIndicator()
    }else{

        LazyColumn(
            state = lazyState,
            modifier = modifier
                .fillMaxSize()
                .background(color = chatBacgroundColor)
                .padding(start = 4.dp, end = 4.dp)
            ,
         //   reverseLayout = true,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            item{
              LoadingCircleProgressNewMessages(state.value.loadingOldMessages)
            }
            //для времени сообщения
            item {
                Spacer(modifier = Modifier.height(8.dp))
            }
            items( state.value.listMessage ) { message ->
                CustomMessageBubble(
                    itemUI = message,
                    modifier = Modifier,
                    optionVisibility = state.value.optionsVisibility,
                    onClickLine = {
                          if (state.value.optionsVisibility){
                              viewModel.onEvent(ConversationEvent.ChangeSelectedMessages(message.messageId))
                          }else{
                              //bottom drawer

                          }
                    },
                    onLongClickLine = {
                        viewModel.onEvent(ConversationEvent.ChangeSelectedMessages(message.messageId))
                    },
                    selected = state.value.selectedMessages[message.messageId] == true

                )
                Spacer(modifier = Modifier.height(8.dp))
            }

        }
    }



}


@Composable
fun MessageBubble(message: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
            .background(
                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                shape = RoundedCornerShape(8.dp)
            )
            .padding(8.dp)
    ) {
        Text(text = message, style = MaterialTheme.typography.bodyMedium)
    }
}