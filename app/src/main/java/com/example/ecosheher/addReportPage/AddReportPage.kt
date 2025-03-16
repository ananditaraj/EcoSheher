package com.example.ecosheher.addReportPage

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.net.Uri
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.ecosheher.R
import com.example.ecosheher.bottomNavPages.BottomNavigationBar
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import java.util.Locale
import android.Manifest
import android.location.Location


@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddReportPage(navController : NavController){

    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("") }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    val context = LocalContext.current

    //location -> joy maa kali , ohm nom shiva
    var currentAddress by remember { mutableStateOf("Click to get location") }
    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }
    // Permission Request Launcher
    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            getCurrentLocation(context, fusedLocationClient) { location ->
                currentAddress = getAddressFromLocation(context, location.latitude, location.longitude)
            }
        } else {
            Toast.makeText(context, "Permission Denied", Toast.LENGTH_SHORT).show()
        }
    }

    val categories = listOf("Roads & StreetLights", "Waste Management", "Water & Utilities", "Parks and Recreation")
    val categoryIcons = mapOf(
        "Roads & StreetLights" to R.drawable.homeicon,
        "Waste Management" to R.drawable.mycity,
        "Water & Utilities" to R.drawable.awareness,
        "Parks and Recreation" to R.drawable.acrossindia
    )

    // Image picker launcher - Opens the gallery and allows the user to select an image
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        selectedImageUri = uri // Store the selected image URI
    }

    // Main UI layout
    Scaffold(
        // Top App Bar with title
        topBar = {
            TopAppBar(
                title = { Text("Add Report", color = Color.White, fontSize = 20.sp) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = colorResource(id = R.color.main_color))
            )
        },
        // Bottom Navigation Bar
        bottomBar = { BottomNavigationBar(navController) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // Image Selection Box
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
                    .clickable { imagePickerLauncher.launch("image/*") } // Opens the gallery
                    .padding(8.dp),
                contentAlignment = Alignment.Center
            ) {
                if (selectedImageUri == null) {
                    // Show default "Choose Image" UI
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            painter = painterResource(id = R.drawable.plusicon), // Placeholder icon
                            contentDescription = "Add Image",
                            tint = Color.Gray,
                            modifier = Modifier.size(40.dp)
                        )
                        Text("Choose an Image", fontSize = 14.sp, color = Color.Black)
                    }
                } else {
                    // Show selected image
                    Image(
                        painter = rememberAsyncImagePainter(model = selectedImageUri),
                        contentDescription = "Selected Image",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Category Selection
            Text(
                "Select Category:", // Title for category selection
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            Column {
                categories.forEach { category ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .border(
                                1.dp,
                                if (selectedCategory == category) Color.Green else Color.Gray,
                                RoundedCornerShape(8.dp)
                            )
                            .background(if (selectedCategory == category) Color(0xFFDFFFE1) else Color.White)
                            .clickable { selectedCategory = category }
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        categoryIcons[category]?.let { iconResId ->
                            Icon(
                                painter = painterResource(id = iconResId),
                                contentDescription = category,
                                tint = Color.Black,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(category, fontSize = 14.sp, color = Color.Black)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Title Input Field
            Text(
                "Title:",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
                )
            BasicTextField(
                value = title,
                onValueChange = { title = it },
                textStyle = TextStyle(fontSize = 14.sp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
                    .padding(12.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Description Input Field
            Text(
                "Description:",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
                )
            BasicTextField(
                value = description,
                onValueChange = { description = it },
                textStyle = TextStyle(fontSize = 14.sp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .padding(8.dp)
                    .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
                    .padding(12.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(9.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        "Click to add your current location",
                        fontSize = 14.sp,
                        color = Color.Black,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp)) 
                    Text(
                        text = currentAddress, // Display the fetched address
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }
                Icon(
                    painter = painterResource(id = R.drawable.locationicon),
                    contentDescription = "Location",
                    tint = Color.Black,
                    modifier = Modifier
                        .size(25.dp)
                        .clickable{
                           // Toast.makeText(context, "Location Clicked", Toast.LENGTH_SHORT).show()
                            if (ContextCompat.checkSelfPermission(
                                    context,
                                    Manifest.permission.ACCESS_FINE_LOCATION
                                ) == PackageManager.PERMISSION_GRANTED
                            ) {
                                getCurrentLocation(context, fusedLocationClient) { location ->
                                    currentAddress = getAddressFromLocation(context, location.latitude, location.longitude)
                                }
                            } else {
                                locationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                            }
                        }
                )
            }
        }
    }
}



// Function to Get Current Location
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


// Function to Convert Latitude & Longitude to Address
fun getAddressFromLocation(context: Context, latitude: Double, longitude: Double): String {
    return try {
        val geocoder = Geocoder(context, Locale.getDefault())
        val addresses: List<Address>? = geocoder.getFromLocation(latitude, longitude, 1)
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
