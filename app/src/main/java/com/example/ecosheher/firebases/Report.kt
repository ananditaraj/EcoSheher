package com.example.ecosheher.firebases

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

data class Report(
    val imageUrl: String = "",
    val title: String = "",
    val description: String = "",
    val category: String = "",
    val location: String = "",
    val userId: String = ""
)

object FirestoreHelper {
    private val db = FirebaseFirestore.getInstance()

    fun saveReport(report: Report, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        db.collection("reports")
            .add(report)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { e -> onFailure(e) }
    }

    fun getReports(userId: String, onSuccess: (List<Report>) -> Unit, onFailure: (Exception) -> Unit) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        db.collection("reports")
            .whereEqualTo("userId", userId)
            .get()
            .addOnSuccessListener { result ->
                val reports = result.documents.map { it.toObject(Report::class.java)!! }
                onSuccess(reports)
            }
            .addOnFailureListener { e -> onFailure(e) }
    }
}
