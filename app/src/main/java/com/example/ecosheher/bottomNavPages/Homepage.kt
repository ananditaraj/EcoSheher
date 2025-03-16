package com.example.ecosheher.bottomNavPages


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
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
import androidx.navigation.NavController
import com.example.ecosheher.R

import com.example.ecosheher.authentication.AuthState
import com.example.ecosheher.authentication.AuthViewModel
import com.example.ecosheher.navGraph.Routes
import com.example.ecosheher.ui.theme.opansnaps

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomePage(modifier: Modifier = Modifier, navController: NavController, authViewModel: AuthViewModel) {
    val authState = authViewModel.authState.observeAsState()

    LaunchedEffect(authState.value) {
        when (authState.value) {
            is AuthState.Unauthenticated -> navController.navigate("login")
            else -> Unit
        }
    }

    Scaffold(
        containerColor = Color.White,
        topBar = {
            TopAppBar(
                modifier = Modifier.height(80.dp),
                colors = TopAppBarDefaults.topAppBarColors(containerColor = colorResource(id = R.color.main_color)),
                title = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "EcoSheher",
                            color = Color.White,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.weight(1f)
                        )

                        IconButton(onClick = {

                            navController.navigate(Routes.MyAccount.routes)
                        }) {
                            Icon(
                                painter = painterResource(id = R.drawable.authimg),
                                contentDescription = "Profile",
                                tint = Color.White,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                }
            )
        },
        bottomBar = { BottomNavigationBar(navController) }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "Welcome to EcoSheher!", fontSize = 22.sp, color = Color.Black)
        }
    }


}


@Composable
fun BottomNavigationBar(navController: NavController) {
    val selectedIndex = remember { mutableStateOf(0) }
    val items = listOf(
        "Home" to R.drawable.homeicon,
        "MyCity" to R.drawable.mycity,
        "Add" to R.drawable.plusicon,
        "AcrossIndia" to R.drawable.acrossindia,
        "Awareness" to R.drawable.awareness
    )

    BottomAppBar(
        modifier = Modifier
            .height(60.dp)
            .clip(RoundedCornerShape(topStart = 18.dp, topEnd = 18.dp)),
        containerColor = Color.LightGray,
        tonalElevation = 8.dp
    ) {
        items.forEachIndexed { index, item ->
            if (item.first == "Add") {

                FloatingActionButton(
                    onClick = {
                         navController.navigate(Routes.AddReport.routes)
                    },
                    containerColor = colorResource(id = R.color.main_color),
                    modifier = Modifier.size(50.dp)
                ) {
                    Icon(
                        painter = painterResource(id = item.second),
                        contentDescription = "Add",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }
            } else {
                IconButton(
                    onClick = {
                        selectedIndex.value = index
                        when (index) {
                            0 -> navController.navigate(Routes.Home.routes)
                            1 -> navController.navigate(Routes.MyCity.routes)
                            3 -> navController.navigate(Routes.AcrossIndia.routes)
                            4 -> navController.navigate(Routes.Awareness.routes)
                        }
                    },
                    modifier = Modifier.weight(1f)
                ) {

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            painter = painterResource(id = item.second),
                            contentDescription = item.first,
                            modifier = Modifier.size(20.dp),
                            tint = if (selectedIndex.value == index) colorResource(id = R.color.main_color) else Color.Black
                        )
                        Text(
                            text = item.first,
                            fontSize = 10.sp,
                            fontFamily = opansnaps,
                            fontWeight = FontWeight.Bold,
                            color = if (selectedIndex.value == index) colorResource(id = R.color.main_color) else Color.Black
                        )
                    }

                }

            }

        }
    }


}