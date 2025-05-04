package com.example.unmei.presentation.chat_list_feature.components
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.unmei.R

import com.example.unmei.presentation.Navigation.Screens
import com.example.unmei.presentation.chat_list_feature.model.NavigationItemDrawer

@Composable
fun DrawerContentFull(
    navController: NavController,
    fullName:String,
    iconUrl:String,
    signInData:String,
    iconOnClick: ()->Unit,
    onClickExitAccount:()->Unit,
    onClickItemDrawer:(NavigationItemDrawer)->Unit
) {
    //90CAF9 - светлый
    //AED6F1 - теплый
    val navigationItemDrawers= listOf(
        NavigationItemDrawer(
            title="Профиль",
            imageVector= ImageVector.vectorResource(id = R.drawable.account_circle_24px),
            navRoute = Screens.Profile.route
        ),
        NavigationItemDrawer(
            title="Друзья",
            imageVector= ImageVector.vectorResource(id = R.drawable.diversity_3_24px),
            navRoute = Screens.Friends.route
        ),
        NavigationItemDrawer(
            title="Настройки",
            imageVector= ImageVector.vectorResource(id = R.drawable.settings_24px),
            navRoute = ""
        ),
        NavigationItemDrawer(
            title="Выход",
            imageVector=ImageVector.vectorResource(id = R.drawable.exit_to_app_24px),
            navRoute = Screens.Main.route,
            colorIcon = Color.Red
        )
    )
    Column(
        modifier= Modifier
            .fillMaxHeight()
            //hard code для //xiaomi mi 9 se
            .width(LocalConfiguration.current.screenWidthDp.dp * 0.85f)
            .verticalScroll(rememberScrollState())
        ,
    ) {
        //блок аватарки
        DrawerIconBlock(
            fullName = fullName,
            iconUrl = iconUrl,
            signInData =signInData,
            iconOnClick =iconOnClick
        )
        //блок драйвера
        NavigationDrawerBlock(
            onClickExitAccount=onClickExitAccount,
            navigationDrawerListItems = navigationItemDrawers,
            onClickItemDrawer = onClickItemDrawer
        )

    }
}
@Composable
fun NavigationDrawerBlock(
    onClickExitAccount:()->Unit,
    navigationDrawerListItems:List<NavigationItemDrawer>,
    onClickItemDrawer:(NavigationItemDrawer)->Unit
){

    val sizeList= remember {
        mutableStateOf(navigationDrawerListItems.size-1)
    }

    Box(
        modifier= Modifier
            .fillMaxWidth()
            .fillMaxHeight()
          //  .fillMaxHeight(0.7f)
    ){
//        Image(
//
//            painter = painterResource(id = R.drawable.subtle_prism), contentDescription = "",
//            contentScale = ContentScale.FillBounds,
//
//        )
        Column (Modifier.fillMaxSize()){
            navigationDrawerListItems.forEachIndexed {
                    index, it->
                if(index==sizeList.value-1){
                    HorizontalDivider()
                }
                if(index!=sizeList.value){
                   // Spacer(modifier = Modifier.height(10.dp))
                    NavigationDrawerItemAdvanced(
                        item = it,
                        onClickItemDrawer = onClickItemDrawer
                    )
                }
            }
            val exitItem = navigationDrawerListItems.last()
           // Spacer(modifier = Modifier.height(10.dp))
            NavigationDrawerItemAdvanced(
                onClickItemDrawer = {
                    onClickExitAccount()
                },
                item = exitItem
            )

        }
    }
}
@Composable
fun NavigationDrawerItemAdvanced(
    onClickItemDrawer:(NavigationItemDrawer)->Unit,
    item:NavigationItemDrawer
){
    NavigationDrawerItem(
        label = {
            Text(
                text = item.title,
                fontSize = 18.sp,
                fontWeight = FontWeight.Normal,
            )
        },
        selected = false,
        onClick = {
            onClickItemDrawer(item)
        },
        icon = {
            Icon(
                modifier = Modifier.width(30.dp),
                imageVector =item.imageVector,
                contentDescription ="",
                tint = item.colorIcon
            )
        },
        modifier = Modifier
            .padding(NavigationDrawerItemDefaults.ItemPadding)
    )
}
@Composable
fun DrawerIconBlock(
    fullName:String,
    iconUrl:String,
    signInData:String,
    iconOnClick: ()->Unit
){
    val colorProfile = Color(0xFFFF9457)

    val colorText = Color(0xFFFFFFFF)
    val colorTextLight = Color(0xFF90CAF9)
    val imageModifier= Modifier
        .size(90.dp)
        .clip(CircleShape)
    Box(
        modifier= Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.3f)
            .background(color = colorProfile)
    ){
        Column (
            modifier= Modifier
                .padding(start=15.dp,top=20.dp),
        ){
            //фото пользователя
            Image(
                modifier = imageModifier.clickable {
                    iconOnClick()
                }
                ,painter = rememberAsyncImagePainter(model = iconUrl),
                contentDescription ="",
                contentScale = ContentScale.Crop//обрезает изображение по центру в доступное пространство
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = fullName,
                //fontFamily = lexendFontFamily,
                fontWeight = FontWeight.Medium,
                fontSize = 18.sp,
                color=colorText
            )
            Spacer(modifier =Modifier.height(6.dp) )
            Text(
                text = signInData,
                //fontFamily = lexendFontFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp,
                color = colorTextLight
            )
        }

    }
}
