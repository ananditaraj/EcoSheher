package com.example.ecosheher.bottomNavPages


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ecosheher.R
import androidx.navigation.NavController

import com.example.ecosheher.authentication.AuthState
import com.example.ecosheher.authentication.AuthViewModel

@Composable
fun HomePage(modifier: Modifier = Modifier, navController: NavController, authViewModel: AuthViewModel) {
    val authState = authViewModel.authState.observeAsState()

    LaunchedEffect(authState.value) {
        when (authState.value) {
            is AuthState.Unauthenticated -> navController.navigate("login")
            else -> Unit
        }
    }

    Text(text = "Home Pagesss")


}

//
//@Composable
//fun BottomNavigationBar(navController: NavController) {
//    var selectedIndex by remember { mutableStateOf(0) }
//    val items = listOf(
//        "Home" to R.drawable.homeicon,
//        "MyCity" to R.drawable.homeicon,
//        "Add" to R.drawable.homeicon,
//        "AcrossIndia" to R.drawable.homeicon,
//        "Awareness" to R.drawable.homeicon
//    )
//
//    BottomAppBar(
//        modifier = Modifier
//            .height(60.dp)
//            .clip(RoundedCornerShape(topStart = 18.dp, topEnd = 18.dp)),
//        containerColor = colorResource(id = R.color.main_color),
//        tonalElevation = 8.dp
//    ) {
//        Row(
//            modifier = Modifier.fillMaxWidth(),
//            verticalAlignment = Alignment.CenterVertically,
//            horizontalArrangement = Arrangement.SpaceEvenly
//        ) {
//            items.forEachIndexed { index, item ->
//                if (item.first == "Add") {
//                    FloatingActionButton (
//                        onClick = { /* Handle center button click */ },
//                        containerColor = colorResource(id = R.color.main_color),
//                        modifier = Modifier.size(55.dp)
//                    ) {
//                        Icon(
//                            painter = painterResource(id = R.drawable.homeicon),
//                            contentDescription = "Add",
//                            tint = Color.White,
//                            modifier = Modifier.size(28.dp)
//                        )
//                    }
//                } else {
//                    IconButton(
//                        onClick = {
//                            selectedIndex = index
//                            when (item.first) {
//                                "Home" -> navController.navigate("home")
//                                "MyCity" -> navController.navigate("mycity")
//                                "AcrossIndia" -> navController.navigate("acrossIndia")
//                                "Awareness" -> navController.navigate("awareness")
//                            }
//                        },
//                        modifier = Modifier.weight(1f)
//                    ) {
//                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
//                            Icon(
//                                painter = painterResource(id = item.second),
//                                contentDescription = item.first,
//                                modifier = Modifier.size(24.dp),
//                                tint = if (selectedIndex == index)
//                                    colorResource(id = R.color.main_color)
//                                else
//                                    Color.Black
//                            )
//                            Text(
//                                text = item.first,
//                                fontSize = 12.sp,
//                                fontWeight = FontWeight.Bold,
//                                color = if (selectedIndex == index)
//                                    colorResource(id = R.color.main_color)
//                                else
//                                    Color.Black
//                            )
//                        }
//                    }
//                }
//            }
//        }
//    }


//}