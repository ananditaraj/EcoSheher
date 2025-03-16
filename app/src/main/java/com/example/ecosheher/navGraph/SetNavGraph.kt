package com.example.ecosheher.navGraph

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.ecosheher.addReportPage.AddReportPage

import com.example.ecosheher.authentication.AuthViewModel
import com.example.ecosheher.authentication.LoginPage
import com.example.ecosheher.authentication.SignUpPage
import com.example.ecosheher.bottomNavPages.AcrossIndiaPage
import com.example.ecosheher.bottomNavPages.AwarenessPage
import com.example.ecosheher.bottomNavPages.HomePage
import com.example.ecosheher.bottomNavPages.MyCityPage
import com.example.ecosheher.myAccount.MyAccountPage


@Composable
fun SetNavGraph(modifier: Modifier = Modifier, authViewModel: AuthViewModel){
    val navController: NavHostController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Routes.Login.routes
    ) {
        composable(Routes.Login.routes) {
            LoginPage(modifier,navController,authViewModel)
        }
        composable(Routes.SignUp.routes) {
            SignUpPage(modifier,navController,authViewModel)
        }
        composable(Routes.Home.routes) {
            HomePage(modifier,navController,authViewModel)
        }
        composable(Routes.MyCity.routes) {
            MyCityPage()
        }
        composable(Routes.AcrossIndia.routes) {
            AcrossIndiaPage()
        }
        composable(Routes.Awareness.routes) {
            AwarenessPage()
        }
        composable(Routes.MyAccount.routes) {
            MyAccountPage()
        }
        composable(Routes.AddReport.routes) {
            AddReportPage(navController)
        }
    }
}