package com.example.ecosheher.firebases

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

    // Fetch reports by location
    fun getReportsByLocation(location: String, onSuccess: (List<Report>) -> Unit) {
        db.collection("reports")
            .whereEqualTo("location", location)
            .get()
            .addOnSuccessListener { result ->
                val reports = result.documents.mapNotNull { it.toObject(Report::class.java) }
                onSuccess(reports)
            }
            .addOnFailureListener { e -> onSuccess(emptyList()) } // Handle failure gracefully
    }
}
