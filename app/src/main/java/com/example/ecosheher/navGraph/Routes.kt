package com.example.ecosheher.navGraph

sealed class Routes(val routes: String) {


    // Firebase Auth
    object SignUp : Routes("signup")
    object Login : Routes("login")

    // Main Pages
    object Home : Routes("homepage")
    object MyCity : Routes("mycity")
    object AcrossIndia : Routes("acrossindia")
    object Awareness : Routes("awareness")

    //My Account
    object MyAccount : Routes("myaccount")

    //Add Reports
    object AddReport : Routes("addreport")

    //Issue Details Screen
    object IssueDetails : Routes("issuedetails")
}