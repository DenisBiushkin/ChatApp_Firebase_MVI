package com.example.unmei.presentation.conversation_future.components

import android.net.Uri
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandIn
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectVerticalDragGestures

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column



import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet



import androidx.compose.material3.Scaffold

import androidx.compose.material3.SheetState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState


import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter

import com.example.unmei.R
import com.example.unmei.presentation.conversation_future.ContentResolverClient
import com.example.unmei.presentation.conversation_future.model.ConversationContentState
import com.example.unmei.presentation.conversation_future.model.ConversationEvent
import com.example.unmei.presentation.conversation_future.model.ConversationVMState
import com.example.unmei.presentation.conversation_future.model.MessageType
import com.example.unmei.presentation.conversation_future.utils.BaseRowWithSelectItemMessage
import com.example.unmei.presentation.conversation_future.utils.BottomBarChatScreen
import com.example.unmei.presentation.conversation_future.utils.BottomButtonSelectMedia

import com.example.unmei.presentation.conversation_future.utils.ChatBubbleImages
import com.example.unmei.presentation.conversation_future.utils.ChatBubbleWithPattern
import com.example.unmei.presentation.conversation_future.utils.LoadingCircleProgressNewMessages
import com.example.unmei.presentation.conversation_future.utils.MessageContent
import com.example.unmei.presentation.conversation_future.utils.TimeMessage
import com.example.unmei.presentation.conversation_future.utils.TopBarChatScreen
import com.example.unmei.presentation.conversation_future.viewmodel.ConversationViewModel
import com.example.unmei.presentation.util.ui.theme.chatBacgroundColor
import com.example.unmei.presentation.util.ui.theme.colorApp
import com.example.unmei.util.ConstansDev.TAG
import kotlin.math.roundToInt

@RequiresApi(35)
@Preview(showBackground = true)
@Composable
fun showChatScreen(){
   ChatScreen(
       navController = rememberNavController(),
       viewModel = hiltViewModel()
   )
}
@RequiresApi(35)
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
                iconChatPainter = rememberAsyncImagePainter(model =state.value.chatIconUrl),
                titleChat = state.value.chatFullName,
                statusChat = "offline"
            )

            AnimatedVisibility(
                visible = state.value.optionsVisibility
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
                            ,
                            onClick = {
                                viewModel.onEvent(ConversationEvent.Offoptions)
                            }
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
                            onClick = { viewModel.onEvent(ConversationEvent.DeleteSelectedMessages) }
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
        },
        bottomBar = {
            BottomBarChatScreen(
                onClickContent = {
                    viewModel.onEvent(ConversationEvent.OpenCloseBottomSheet)
                },
                onSendMessage = {
                    viewModel.onEvent(ConversationEvent.SendMessage(it))

                }
            )
        },
        //учитывает появление клавиатуры
        modifier = Modifier.imePadding()
    ) {
        paddingValues ->
        when(state.value.contentState){
            ConversationContentState.EmptyType ->{
                ChatScreenEmptyContent(
                    modifier = Modifier.padding(paddingValues),
                    backgroundColor = chatBacgroundColor
                )
            }
            ConversationContentState.Loading -> {
                    CircularProgressIndicator()
                }
            ConversationContentState.Messaging -> {
                ContentChatScreen(
                    modifier = Modifier.padding(paddingValues),
                    state = state,
                    lazyState= lazyState,
                    viewModel
                )
            }
        }


        Log.d(TAG,state.value.selectedMessages.keys.toList().toString())

        ConversationModalBottom(
            bottomSheetState = bottomSheetState,
            visibility = state.value.bottomSheetVisibility,
            onDismissRequest = {
                viewModel.onEvent(ConversationEvent.OpenCloseBottomSheet)
            },
            onSelectedMedia = {
                viewModel.onEvent(ConversationEvent.SelectedMediaToSend(it))
            }
        )


        
    }
}



@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun ConversationModalBottom(
    bottomSheetState: SheetState,
    visibility :Boolean = false,
    onDismissRequest: ()->Unit,
    onSelectedMedia:(List<Uri>)->Unit
){
    val screenSettings= LocalConfiguration.current
    val sizeOneItemDp=screenSettings.screenWidthDp.dp /3
    val maxHeihtBottomSheet = screenSettings.screenHeightDp.dp * 0.7f

    val selectedImages = remember { mutableStateOf<List<Uri>>(emptyList()) }
    val listImageUris = remember { mutableStateOf<List<Uri>>(emptyList()) }
    val isLoadingImages = remember { mutableStateOf<Boolean>(false) }


    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    val doTrash = remember {
        mutableStateOf(false)
    }
   if (doTrash .value){
        Popup(
            onDismissRequest = {},
            properties = PopupProperties(focusable = true)
        ) {
            var offsetY by remember { mutableStateOf(0f) }
            val maxHeight = 400.dp // Максимальная высота bottom sheet
            val minHeight = 100.dp // Минимальная высота bottom sheet

            // Анимация для плавного изменения высоты
            val animatedOffsetY by animateFloatAsState(
                targetValue = offsetY,
                animationSpec = tween(durationMillis =0)
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.5f)) // Затемняющий фон
                    .pointerInput(Unit) {
                        detectTapGestures {
                            //  onDismiss() // Закрыть при нажатии на scrim
                        }
                    }
                , contentAlignment = Alignment.BottomEnd
            ) {
                // Содержимое bottom sheet
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(maxHeight)
                        .offset {
                            IntOffset(
                                0,
                                animatedOffsetY.roundToInt()
                            )
                        }
                        .pointerInput(Unit) {
                            detectVerticalDragGestures(
                                onDragStart = { offset ->
                                    // Начало перетаскивания (опционально)
                                },
                                onDragEnd = {
                                    // Закрыть, если пользователь перетащил вниз
                                    if (offsetY > -maxHeight.toPx() / 2) {
                                        // onDismiss()
                                    } else {
                                        offsetY = -maxHeight.toPx()
                                    }
                                },
                                onVerticalDrag = { change, dragAmount ->
                                    offsetY = (offsetY + dragAmount)
                                        .coerceIn(
                                            -maxHeight.toPx(), -minHeight.toPx()
                                        )
                                }
                            )
                        },
                    shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
                    color = Color.White
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                    ) {
                        Text("Это кастомный Bottom Sheet", style = MaterialTheme.typography.titleMedium)
                        Spacer(modifier = Modifier.height(16.dp))
                       // content()
                    }
                }
            }
        }
   }
    if (visibility && !doTrash.value) {

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
                                onSelectedMedia(selectedImages.value)
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
                    listImageUris.value=ContentResolverClient(context).getAllImagesUri().take(20)
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
                        ,columns =GridCells.Fixed(3),
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

@Composable
fun CircleCountIndicatorSelectedItem(
    modifier: Modifier = Modifier,
    isSelected:Boolean = false,
    count:Int = 1,
    onClickRound:()->Unit
){

    Box(
        modifier = modifier
            .background(
                shape = CircleShape,
                color = Color.Transparent
            )
            .size(25.dp)
            .border(
                width = 2.dp,
                color = if (isSelected) Color.White else colorApp,
                shape = CircleShape,
            )
            .clickable(
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
    modifier: Modifier = Modifier,
    visibility: Boolean,

) {
   if(visibility){
     Box(
         modifier = modifier
             .heightIn(min = 60.dp, max = 60.dp)
             .fillMaxWidth()
           //  .background(Color.LightGray)
         ,
         contentAlignment = Alignment.Center,

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
        LazyColumn(
            state = lazyState,
            modifier = modifier
                .fillMaxSize()
                .background(color = chatBacgroundColor)
                .padding(start = 4.dp, end = 4.dp)
            ,
            reverseLayout = true,

            
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            item{
              LoadingCircleProgressNewMessages(state.value.loadingOldMessages)
            }
//            state.value.groupedMapMessage.forEach{
//                (date,messages)->
                items(  state.value.listMessage) { message ->
                    BaseRowWithSelectItemMessage(
                        optionVisibility = state.value.optionsVisibility,
                        onClickLine = {
                            if (state.value.optionsVisibility){
                                viewModel.onEvent(ConversationEvent.ChangeSelectedMessages(message.messageId))
                            }else{
                                //bottom drawer
                            }
                        },onLongClickLine = {
                            viewModel.onEvent(ConversationEvent.ChangeSelectedMessages(message.messageId))
                        },
                        selected = state.value.selectedMessages[message.messageId] == true
                    ) {
                        when(message.type){
                            is MessageType.Image -> {
                                ChatBubbleImages(item = message)
                            }
                            is  MessageType.Text -> {
                                ChatBubbleWithPattern(
                                    modifier= Modifier,
                                    isOwn = message.isOwn,) {
                                    MessageContent(
                                        data = message
                                    )
                                }
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }
//                item {
//                    TimeMessage(time = date.toString())
//                    Spacer(modifier = Modifier.height(8.dp))
//                }

            }




            if(!state.value.selectedUrisForRequest.isEmpty()){
                viewModel.testFun()
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