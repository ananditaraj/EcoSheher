//package com.example.ecosheher.firebases
//
//import android.util.Log
//import com.google.firebase.firestore.FirebaseFirestore
//
//data class Report(
//    val imageUrl: String = "",
//    val title: String = "",
//    val description: String = "",
//    val category: String = "",
//    val location: String = "",
//    val userId: String = "",
//    var upvoteCount: Int = 0,
//    var reportId: String = "",
//    val timestamp: Long = System.currentTimeMillis()
//)
//
//object FirestoreHelper {
//    private val db = FirebaseFirestore.getInstance()
//
//    // Save a report
//    fun saveReport(report: Report, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
//        db.collection("reports")
//            .add(report)
//            .addOnSuccessListener { documentReference ->
//                val reportId = documentReference.id
//                val updatedReport = report.copy(reportId = reportId, timestamp = System.currentTimeMillis())
//                db.collection("reports").document(reportId)
//                    .set(updatedReport)
//                    .addOnSuccessListener { onSuccess() }
//                    .addOnFailureListener { e -> onFailure(e) }
//            }
//            .addOnFailureListener { e -> onFailure(e) }
//    }
//
//    // Get reports for a user
//    fun getReports(userId: String, onSuccess: (List<Report>) -> Unit, onFailure: (Exception) -> Unit) {
//        db.collection("reports")
//            .whereEqualTo("userId", userId)
//            .get()
//            .addOnSuccessListener { result ->
//                val reports = result.documents.mapNotNull { it.toObject(Report::class.java) }
//                onSuccess(reports)
//            }
//            .addOnFailureListener { e -> onFailure(e) }
//    }
//
//    // Get a single report
//    fun getReport(reportId: String, onSuccess: (Report) -> Unit, onFailure: (Exception) -> Unit) {
//        db.collection("reports").document(reportId).get()
//            .addOnSuccessListener { document ->
//                if (document.exists()) {
//                    val report = document.toObject(Report::class.java)
//                    if (report != null) {
//                        onSuccess(report)
//                    } else {
//                        onFailure(Exception("Failed to parse report"))
//                    }
//                } else {
//                    onFailure(Exception("Report not found"))
//                }
//            }
//            .addOnFailureListener { exception -> onFailure(exception) }
//    }
//
//    // Update upvote count
//    fun updateUpvoteCount(reportId: String, newCount: Int, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
//        db.collection("reports").document(reportId)
//            .update("upvoteCount", newCount)
//            .addOnSuccessListener { onSuccess() }
//            .addOnFailureListener { onFailure(it) }
//    }
//
//    // Fetch reports by location
////    fun getReportsByLocation(location: String, onSuccess: (List<Report>) -> Unit) {
////        db.collection("reports")
////            .whereEqualTo("location", location)
////            .get()
////            .addOnSuccessListener { result ->
////                val reports = result.documents.mapNotNull { it.toObject(Report::class.java) }
////                onSuccess(reports)
////            }
////            .addOnFailureListener { e -> onSuccess(emptyList()) } // Handle failure gracefully
////    }
//
//    fun getReportsByLocation(location: String, onSuccess: (List<Report>) -> Unit) {
//        db.collection("reports")
//            .whereEqualTo("location", location)
//            .get()
//            .addOnSuccessListener { result ->
//                val reports = result.documents.mapNotNull { it.toObject(Report::class.java) }
//                Log.d("Firestore", "Fetched reports: ${reports.size}") // Debugging logs
//                onSuccess(reports)
//            }
//            .addOnFailureListener { e ->
//                Log.e("Firestore", "Error fetching reports: ", e)
//                onSuccess(emptyList())
//            }
//    }
//
//}
package com.example.ecosheher.firebases

import android.content.Context
import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore

data class Report(
    val imageUrl: String = "",
    val title: String = "",
    val description: String = "",
    val category: String = "",
    val location: String = "",
    val userId: String = "",
    var upvoteCount: Int = 0,
    var reportId: String = "",
    val timestamp: Long = System.currentTimeMillis()
)

object FirestoreHelper {
    private val db = FirebaseFirestore.getInstance()

    // Save a report
    fun saveReport(report: Report, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        db.collection("reports")
            .add(report)
            .addOnSuccessListener { documentReference ->
                val reportId = documentReference.id
                val updatedReport = report.copy(reportId = reportId, timestamp = System.currentTimeMillis())
                db.collection("reports").document(reportId)
                    .set(updatedReport)
                    .addOnSuccessListener { onSuccess() }
                    .addOnFailureListener { e -> onFailure(e) }
            }
            .addOnFailureListener { e -> onFailure(e) }
    }

    // Get reports for a user
    fun getReports(userId: String, onSuccess: (List<Report>) -> Unit, onFailure: (Exception) -> Unit) {
        db.collection("reports")
            .whereEqualTo("userId", userId)
            .get()
            .addOnSuccessListener { result ->
                val reports = result.documents.mapNotNull { it.toObject(Report::class.java) }
                onSuccess(reports)
            }
            .addOnFailureListener { e -> onFailure(e) }
    }

    // Get a single report
    fun getReport(reportId: String, onSuccess: (Report) -> Unit, onFailure: (Exception) -> Unit) {
        db.collection("reports").document(reportId).get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val report = document.toObject(Report::class.java)
                    if (report != null) {
                        onSuccess(report)
                    } else {
                        onFailure(Exception("Failed to parse report"))
                    }
                } else {
                    onFailure(Exception("Report not found"))
                }
            }
            .addOnFailureListener { exception -> onFailure(exception) }
    }

    // Update upvote count
    fun updateUpvoteCount(reportId: String, newCount: Int, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        db.collection("reports").document(reportId)
            .update("upvoteCount", newCount)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onFailure(it) }
    }

    fun getReportsByLocation(location: String, onSuccess: (List<Report>) -> Unit) {
        if (location.isBlank()) {
            Log.e("Firestore", "Invalid location: Cannot be empty")
            onSuccess(emptyList())
            return
        }

        db.collection("reports")
            .whereEqualTo("location", location)
            .get()
            .addOnSuccessListener { result ->
                val reports = result.documents.mapNotNull { document ->
                    val report = document.toObject(Report::class.java)
                    report?.copy(reportId = document.id)  // Assign document ID as reportId
                }
                Log.d("Firestore", "Fetched ${reports.size} reports for location: $location")
                onSuccess(reports)
            }
            .addOnFailureListener { e ->
                Log.e("Firestore", "Error fetching reports: ", e)
                onSuccess(emptyList()) // Return empty list on failure
            }
    }

    fun getAllReports(onSuccess: (List<Report>) -> Unit) {
        db.collection("reports")
            .get()
            .addOnSuccessListener { result ->
                val reports = result.documents.mapNotNull { document ->
                    val report = document.toObject(Report::class.java)
                    report?.copy(reportId = document.id)  // Assign Firestore document ID
                }
                Log.d("Firestore", "Fetched ${reports.size} reports in total")
                onSuccess(reports)
            }
            .addOnFailureListener { e ->
                Log.e("Firestore", "Error fetching all reports: ", e)
                onSuccess(emptyList())  // Return empty list on failure
            }
    }

    // Check if user has upvoted
    fun hasUserUpvoted(context : Context, reportId: String): Boolean {
        val sharedPreferences = context.getSharedPreferences("upvotes", Context.MODE_PRIVATE)
        return sharedPreferences.getBoolean(reportId, false)
    }

    // Mark user as having upvoted
    fun markUserUpvoted(context : Context,reportId: String) {
        val sharedPreferences = context.getSharedPreferences("upvotes", Context.MODE_PRIVATE)
        sharedPreferences.edit().putBoolean(reportId, true).apply()
    }


}
