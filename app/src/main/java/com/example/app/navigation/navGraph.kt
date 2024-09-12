package com.example.app.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.app.screens.BottomNavigation
import com.example.app.screens.Home
import com.example.app.screens.Login
import com.example.app.screens.Notification
import com.example.app.screens.Otheruser
import com.example.app.screens.Post
import com.example.app.screens.Profile
import com.example.app.screens.Register
import com.example.app.screens.Search
import com.example.app.screens.Splash

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Routes.Splash.route) {
        composable(Routes.Splash.route) {
            Splash(navController = navController)
        }
        composable(Routes.Home.route) {
            Home(navController)
        }
        composable(Routes.Post.route) {
            Post(navController)
        }
        composable(Routes.Notification.route) {
            Notification()
        }
        composable(Routes.Profile.route) {
            Profile(navController)
        }
        composable(Routes.Search.route) {
            Search(navController)
        }
        composable(Routes.BottomNavigation.route) {
            BottomNavigation(navController = navController)
        }
        composable(Routes.Login.route) {
            Login(navController)
        }
        composable(Routes.Register.route) {
            Register(navController)
        }

        composable(Routes.OtherUser.route) {

            val data = it.arguments!!.getString("data")
            Otheruser(navController,data!!)
        }
    }
}