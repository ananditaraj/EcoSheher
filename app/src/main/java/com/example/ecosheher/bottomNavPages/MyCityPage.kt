package com.example.ecosheher.bottomNavPages

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.location.Geocoder
import android.location.Location
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.ecosheher.R
import com.example.ecosheher.firebases.FirestoreHelper
import com.example.ecosheher.firebases.Report
import com.example.ecosheher.navGraph.Routes
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.gson.Gson
import kotlinx.coroutines.launch
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyCityPage(navController: NavController) {
    val context = LocalContext.current
    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }
    var currentAddress by remember { mutableStateOf("Fetching location...") }
    var reports by remember { mutableStateOf<List<Report>>(emptyList()) }
    val coroutineScope = rememberCoroutineScope()

    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            getCurrentLocation(context, fusedLocationClient) { location ->
                val address = getAddressFromLocation(context, location.latitude, location.longitude)
                currentAddress = address
                fetchReportsByLocation(address) { fetchedReports -> reports = fetchedReports }
            }
        } else {
            Toast.makeText(context, "Location permission denied", Toast.LENGTH_SHORT).show()
        }
    }

    LaunchedEffect(Unit) {
        if (ContextCompat.checkSelfPermission(
                context, Manifest.permission.ACCESS_FINE_LOCATION
            ) == android.content.pm.PackageManager.PERMISSION_GRANTED
        ) {
            getCurrentLocation(context, fusedLocationClient) { location ->
                val address = getAddressFromLocation(context, location.latitude, location.longitude)
                currentAddress = address
                fetchReportsByLocation(address) { fetchedReports -> reports = fetchedReports }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("My City", color = Color.White, fontSize = 20.sp) },
                modifier = Modifier.height(80.dp),
                colors = TopAppBarDefaults.topAppBarColors(containerColor = colorResource(id = R.color.main_color)),
                actions = {
                    Icon(
                        painter = painterResource(id = R.drawable.locationicon),
                        contentDescription = "Location",
                        tint = Color.White,
                        modifier = Modifier
                            .size(30.dp)
                            .clickable {
                                if (ContextCompat.checkSelfPermission(
                                        context, Manifest.permission.ACCESS_FINE_LOCATION
                                    ) == android.content.pm.PackageManager.PERMISSION_GRANTED
                                ) {
                                    getCurrentLocation(context, fusedLocationClient) { location ->
                                        val address = getAddressFromLocation(context, location.latitude, location.longitude)
                                        currentAddress = address
                                        fetchReportsByLocation(address) { fetchedReports -> reports = fetchedReports }
                                    }
                                } else {
                                    locationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                                }
                            }
                    )
                }
            )
        },
        bottomBar = { BottomNavigationBar(navController) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Current Location: $currentAddress", fontSize = 18.sp, color = Color.Black)
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "Reported Issues in Your City:", fontSize = 20.sp, color = Color.Black)
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(vertical = 10.dp)
            ) {
                items(reports) { report ->
                    ReportItem(report,navController)
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


@SuppressLint("MissingPermission")
fun getCurrentLocation(
    context: Context,
    fusedLocationClient: FusedLocationProviderClient,
    onLocationReceived: (Location) -> Unit
) {
    fusedLocationClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null)
        .addOnSuccessListener { location ->
            if (location != null) {
                onLocationReceived(location)
            } else {
                Toast.makeText(context, "Unable to fetch location", Toast.LENGTH_SHORT).show()
            }
        }
}

fun getAddressFromLocation(context: Context, latitude: Double, longitude: Double): String {
    return try {
        val geocoder = Geocoder(context, Locale.getDefault())
        val addresses = geocoder.getFromLocation(latitude, longitude, 1)
        if (!addresses.isNullOrEmpty()) {
            val address = addresses[0]
            "${address.locality ?: "Unknown"}, ${address.postalCode ?: ""}"
        } else {
            "Location Not Found"
        }
    } catch (e: Exception) {
        "Error Fetching Location"
    }
}

fun fetchReportsByLocation(location: String, onResult: (List<Report>) -> Unit) {
    FirestoreHelper.getReportsByLocation(location, onResult)
}