package com.example.ecosheher.navGraph

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.ecosheher.addReportPage.AddReportPage

import com.example.ecosheher.authentication.AuthViewModel
import com.example.ecosheher.authentication.LoginPage
import com.example.ecosheher.authentication.SignUpPage
import com.example.ecosheher.bottomNavPages.AcrossIndiaPage
import com.example.ecosheher.bottomNavPages.AwarenessPage
import com.example.ecosheher.bottomNavPages.HomePage
import com.example.ecosheher.bottomNavPages.MyCityPage
import com.example.ecosheher.firebases.Report
import com.example.ecosheher.myAccount.MyAccountPage
import com.example.ecosheher.showfullissue.IssueDetailsPage
import com.google.gson.Gson


@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
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
        composable(Routes.MyAccount.routes) {
            MyAccountPage(navController)
        }

        composable(Routes.MyCity.routes) {
            MyCityPage(navController)
        }
        composable(Routes.AcrossIndia.routes) {
            AcrossIndiaPage(navController)
        }
        composable(Routes.Awareness.routes) {
            AwarenessPage()
        }
        composable(Routes.AddReport.routes) {
            AddReportPage(navController)
        }
        composable(
            route = "${Routes.IssueDetails.routes}/{reportJson}",
            arguments = listOf(navArgument("reportJson") { type = NavType.StringType })
        ) { backStackEntry ->
            val reportJson = backStackEntry.arguments?.getString("reportJson")
            val report = Gson().fromJson(reportJson, Report::class.java)

            IssueDetailsPage(
                reportId = report.reportId,
                title = report.title,
                description = report.description,
                category = report.category,
                location = report.location,
                imageUrl = report.imageUrl,
            )
        }

    }
}