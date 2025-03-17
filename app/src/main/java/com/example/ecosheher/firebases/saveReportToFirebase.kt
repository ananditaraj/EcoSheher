package com.example.ecosheher.firebases
import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.navigation.NavController
import com.example.ecosheher.cloudinary.CloudinaryHelper
import com.google.firebase.auth.FirebaseAuth

fun saveReportToFirebase(
    context : Context,
    navController: NavController,
    imageUrl: String,
    title: String,
    description: String,
    category: String,
    location: String,

) {

    val userId = FirebaseAuth.getInstance().currentUser?.uid
    if (userId == null) {
        Toast.makeText(context, "User not logged in", Toast.LENGTH_SHORT).show()
        return
    }
    val report = Report(imageUrl, title, description, category, location, userId)
    FirestoreHelper.saveReport(report,
        onSuccess = {
            Toast.makeText(context, "Report Submitted Successfully", Toast.LENGTH_SHORT).show()
            navController.navigate("myaccount")
        },
        onFailure = { error ->
            Toast.makeText(context, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
        }
    )
}
