package com.example.ecosheher.myAccount

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.R
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import com.example.ecosheher.navGraph.Routes
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyAccountPage(navController: NavController){
    val context = LocalContext.current
    var reports by remember { mutableStateOf(emptyList<Report>()) }

    val userId = FirebaseAuth.getInstance().currentUser?.uid

    LaunchedEffect(Unit) {
        if (userId != null) {
            // Fetch reports list based on userId
            val db = FirebaseFirestore.getInstance()
            db.collection("reports").whereEqualTo("userId", userId).get()
                .addOnSuccessListener { documents ->
                    val reportList = documents.mapNotNull { it.toObject(Report::class.java) }
                    reports = reportList
                }
                .addOnFailureListener { error ->
                    Toast.makeText(context, "Failed to fetch reports: ${error.localizedMessage ?: "Unknown error"}", Toast.LENGTH_SHORT).show()
                }
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
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                   items(reports){ report->
                       ReportItem(report,navController)
                   }
                }
            }
        }
    }
}

@Composable
fun ReportItem(report: Report, navController: NavController) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable {
                val reportJson = Gson().toJson(report)  // Convert object to JSON
                val encodedJson = URLEncoder.encode(reportJson, StandardCharsets.UTF_8.toString())
                    .replace("+", "%20")
                navController.navigate("${Routes.IssueDetails.routes}/$encodedJson")
            },
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .padding(10.dp)
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
            Spacer(modifier = Modifier.height(4.dp))
            Text("Upvotes: ${report.upvoteCount}", fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }
    }
}
