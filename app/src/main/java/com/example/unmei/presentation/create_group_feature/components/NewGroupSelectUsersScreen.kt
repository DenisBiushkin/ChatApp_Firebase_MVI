package com.example.unmei.presentation.create_group_feature.components

import android.Manifest
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.example.unmei.presentation.create_group_feature.model.CreateGroupContentState
import com.example.unmei.presentation.create_group_feature.model.CreateGroupItemUi
import com.example.unmei.presentation.create_group_feature.utils.GroupBottomButtonSelectMedia
import com.example.unmei.presentation.create_group_feature.utils.GroupUserItemListWithIndicator
import com.example.unmei.presentation.create_group_feature.utils.SelectedGroupUser
import com.example.unmei.presentation.create_group_feature.viemodel.CreateGroupViewModel
import com.example.unmei.presentation.util.LoadingScreen
import com.example.unmei.util.ConstansDev.TAG



@Preview(showBackground = true)
@Composable
fun showNewGroupSelectUsersScreen(

){

    Box(Modifier.fillMaxSize()){
        NewGroupSelectUsersScreen(
            navController = rememberNavController()
        )

    }

}

@Composable
fun NewGroupSelectUsersScreen(
    navController: NavController,
    viewModel: CreateGroupViewModel = hiltViewModel()
){


    val state = viewModel.state.collectAsState()
    Scaffold(
        topBar = {
            SelectUsersGroupTopBar(
                onClickBack={
                    navController.popBackStack()
                }
            )
        },
        bottomBar = {
            GroupBottomButtonSelectMedia(
                modifier = Modifier
                    .navigationBarsPadding()
                    .imePadding(),
                countSelectedMedia=state.value.selectedContacts.size,
                onClick={
                    viewModel.createNewChat()
                },
            )
        },
        modifier =Modifier.fillMaxSize()
    ) {
        paddingValues ->
        val permissionLauncher = rememberLauncherForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted ->
            if (isGranted) {
                // разрешение выдано, можно продолжить
            } else {
                // отказано
            }
        }
        val iconLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.PickVisualMedia()
        ) { uri: Uri? ->
            // uri может быть null, если пользователь ничего не выбрал
            if (uri != null) {
                Log.d(TAG, "Выбрано изображение: $uri")
                viewModel.onSelectIconUri(uri)
            } else {
                Log.d(TAG, "Изображение не выбрано")
            }
        }
        Column (
            modifier = Modifier.padding(paddingValues)
        ){
            CompanionTextFieldGroup(
                painter= null,
                onSelectClick={
                    Log.d(TAG,"SELECT IMAGE")
                    permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                    iconLauncher.launch(
                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                    )
                },
                textChatName = state.value.chatName,
                onValueChange = {
                    viewModel.textChatNameChanged(it)
                }
            )
            HorizontalDivider()

            when(state.value.contentState){
                CreateGroupContentState.LOADING -> LoadingScreen()
                CreateGroupContentState.EMPTY -> EmptyCreateGroupScreen()
                CreateGroupContentState.CONTENT -> GroupContent(
                        mapItemUi = state.value.groupedContacts,
                        onClickItem = {
                            viewModel.addUserInChat(it)
                        },
                        selectedContactsMap = state.value.selectedContacts,
                    onClickReject = {
                        viewModel.addUserInChat(it)
                    }
                    )
            }

        }


    }
}
@Composable
fun SelectedUsersBlock(
    selectedUsersMap: Map<String,CreateGroupItemUi>,
    onClickReject:(CreateGroupItemUi)->Unit
){
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp),
        contentAlignment = Alignment.Center
    ){
        if(selectedUsersMap.isEmpty()){
            Text(
                text = "Выберите участников беседы",
                fontSize = 17.sp,
                fontWeight = FontWeight.Medium
            )
        }else{
            LazyRow (
                modifier = Modifier
                    .fillMaxSize()
                , verticalAlignment = Alignment.CenterVertically,
            ){

                items(selectedUsersMap.values.map { it }){
                    SelectedGroupUser(
                        modifier = Modifier
                            .padding(horizontal = 8.dp),
                        painter= rememberAsyncImagePainter(model = it.iconUrl),
                        name=it.fullName.split(" ").first(),
                        onClickReject={
                            onClickReject(it)
                        }
                    )
                }
            }
        }


    }
}
@Composable
fun GroupContent(
    mapItemUi: Map<String,List<CreateGroupItemUi>>,
    selectedContactsMap:   Map<String,CreateGroupItemUi>,
    onClickItem: (CreateGroupItemUi) -> Unit,
    onClickReject: (CreateGroupItemUi) -> Unit
){
    SelectedUsersBlock(
        selectedUsersMap = selectedContactsMap,
        onClickReject = onClickReject
    )
    HorizontalDivider()
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        mapItemUi.forEach{
                data->
            item(){
                MarkerGroup(
                    text=data.key
                )
            }
            items(data.value){
                GroupUserItemListWithIndicator(
                    painter= rememberAsyncImagePainter(model = it.iconUrl),
                    fullName=it.fullName,
                    lastSeen=it.lastSeen,
                    isSelected =selectedContactsMap.keys.contains(it.id),
                    onClick = {onClickItem(it)}
                )
            }
        }
    }
}

@Composable
fun MarkerGroup(
    text:String
){
    HorizontalDivider()
    Row (
        modifier = Modifier.
        padding(vertical =  15.dp)
    ){
        Spacer(modifier = Modifier.width(10.dp))
        Text(
            text=text,
            fontSize = 17.sp,
            fontWeight = FontWeight.Medium
        )
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectUsersGroupTopBar(
    onClickBack:()->Unit
){
    TopAppBar(
        title = {
           Text(
               text = "Новый чат",
               fontWeight = FontWeight.Medium,
               fontSize = 20.sp
           )
        },
        navigationIcon = {
            IconButton(onClick =onClickBack) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = ""
                )
            }
        }
    )
}