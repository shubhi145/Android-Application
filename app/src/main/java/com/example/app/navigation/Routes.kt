package com.example.app.navigation

sealed class Routes(val route: String) {
    object Home : Routes("home")
    object Profile : Routes("profile")
    object Settings : Routes("settings")
    object Post : Routes("post")
    object Search : Routes("search")
    object Splash : Routes("splash")
    object Login : Routes("login")
    object Register : Routes("register")
    object BottomNavigation : Routes("bottom_navigation")
    object Notification : Routes("notification")
    object OtherUser: Routes("other_user/{data}")
}