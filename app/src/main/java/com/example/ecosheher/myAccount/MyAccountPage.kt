package com.example.ecosheher.myAccount

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.R
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.ecosheher.bottomNavPages.BottomNavigationBar
import com.example.ecosheher.firebases.FirestoreHelper
import com.example.ecosheher.firebases.Report
import com.google.firebase.auth.FirebaseAuth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyAccountPage(navController: NavController){
    val context = LocalContext.current
    var reports by remember { mutableStateOf(emptyList<Report>()) }

    LaunchedEffect(Unit) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId != null) {
            FirestoreHelper.getReports(userId,
                onSuccess = { reportList -> reports = reportList },
                onFailure = { error ->
                    Toast.makeText(context, "Failed to fetch reports: ${error.message}", Toast.LENGTH_SHORT).show()
                })
        } else {
            Toast.makeText(context, "User not logged in", Toast.LENGTH_SHORT).show()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("My Reports", color = Color.White, fontSize = 20.sp) },
                modifier = Modifier.height(80.dp),
                colors = TopAppBarDefaults.topAppBarColors(containerColor = colorResource(id = com.example.ecosheher.R.color.main_color)),
            )
        },
        bottomBar = { BottomNavigationBar(navController) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            if (reports.isEmpty()) {
                Text("No reports available", fontSize = 16.sp, color = Color.Gray)
            } else {
//
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                   items(reports){ report->
                       ReportItem(report)
                   }
                }
            }
        }
    }
}

@Composable
fun ReportItem(report: Report) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
            .padding(12.dp)
    ) {
        Image(
            painter = rememberAsyncImagePainter(model = report.imageUrl),
            contentDescription = "Report Image",
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text("Title: ${report.title}", fontWeight = FontWeight.Bold)
        Text("Description: ${report.description}")
        Text("Category: ${report.category}")
        Text("Location: ${report.location}")
    }
}
