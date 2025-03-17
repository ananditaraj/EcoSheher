package com.example.ecosheher.bottomNavPages

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.ecosheher.firebases.FirestoreHelper
import com.example.ecosheher.firebases.Report
import com.example.ecosheher.navGraph.Routes
import com.google.gson.Gson
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@Composable
fun AcrossIndiaPage(navController: NavController) {
    var reports by remember { mutableStateOf<List<Report>>(emptyList()) }

    // Fetch reports when the page is loaded
    LaunchedEffect(Unit) {
        FirestoreHelper.getAllReports { fetchedReports ->
            reports = fetchedReports
            Log.d("Firestore", "Fetched ${reports.size} reports")
        }
    }

    if (reports.isEmpty()) {
        Text("No reports available", modifier = Modifier.padding(16.dp))
    } else {
        LazyColumn(modifier = Modifier.fillMaxSize().padding(8.dp)) {
            items(reports) { report ->
                ReportItems(report, navController)
            }
        }
    }
}

@Composable
fun ReportItems(report: Report, navController: NavController) {
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
