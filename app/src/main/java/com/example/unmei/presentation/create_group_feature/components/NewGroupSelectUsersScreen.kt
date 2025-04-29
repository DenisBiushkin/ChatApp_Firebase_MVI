package com.example.unmei.presentation.create_group_feature.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.input.pointer.motionEventSpy
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.example.unmei.R
import com.example.unmei.presentation.conversation_future.utils.BottomButtonSelectMedia
import com.example.unmei.presentation.create_group_feature.model.CreateGroupContentState
import com.example.unmei.presentation.create_group_feature.model.CreateGroupItemUi
import com.example.unmei.presentation.create_group_feature.utils.GroupBottomButtonSelectMedia
import com.example.unmei.presentation.create_group_feature.utils.GroupUserItemListWithIndicator
import com.example.unmei.presentation.create_group_feature.utils.SelectedGroupUser
import com.example.unmei.presentation.create_group_feature.viemodel.CreateGroupViewModel
import com.example.unmei.presentation.util.LoadingScreen
import com.example.unmei.presentation.util.ui.theme.colorApp


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
                modifier = Modifier.imePadding(),
                countSelectedMedia=2,
                onClick={
                },
            )
        },
        modifier =Modifier.fillMaxSize()
    ) {
        paddingValues ->
        Column (
            modifier = Modifier.padding(paddingValues)
        ){
            CompanionTextFieldGroup(
                painter=null,
                onSelectClick={

                }
            )
            HorizontalDivider()

            when(state.value.contentState){
                CreateGroupContentState.LOADING -> LoadingScreen()
                CreateGroupContentState.EMPTY -> EmptyCreateGroupScreen()
                CreateGroupContentState.CONTENT -> GroupContent(
                        mapItemUi = state.value.groupedFriends,
                        onClickItem = {

                        }
                    )
            }

        }


    }
}
@Composable
fun SelectedUsersBlock(
    selectedUsersMap: Map<String,CreateGroupItemUi>,
    onClickReject:(String)->Unit
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

//                items(selectedUsersMap.values){
//                    SelectedGroupUser(
//                        modifier = Modifier
//                            .padding(horizontal = 8.dp),
//                        painter= selectedUsersMap.,
//                        name="Marcile",
//                        onClickReject={}
//                    )
//                }
            }
        }


    }
}
@Composable
fun GroupContent(
   mapItemUi: Map<String,List<CreateGroupItemUi>>,
   onClickItem: (CreateGroupItemUi) -> Unit
){
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
                    isSelected = true,
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