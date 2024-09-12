package com.example.app.screens

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.window.Notification
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.app.model.BottomNavItem
import com.example.app.navigation.Routes
import java.nio.file.WatchEvent

@Composable

fun BottomNavigation(navController:NavController){

    val navController1 = rememberNavController()

    Scaffold (
        bottomBar = {
            MyBottomBar(navController1)
        }
    ){innerpadding->
        NavHost(navController = navController1,startDestination = Routes.Home.route,
            modifier = WatchEvent.Modifier.padding(innerpadding)){
              composable(route = Routes.Home.route){
                  Home(navController)
              }
            composable(route = Routes.Profile.route){
                Profile(navController)
            }
            composable(route = Routes.Post.route){
                Post(navController1)
            }
            composable(route = Routes.Notification.route){
                Notification()
            }
            composable(route = Routes.Search.route){
                Search(navController1)
            }

        }
    }
}

@Composable
fun MyBottomBar(navController1: NavController){

    val backStackEntery = navController1.currentBackStackEntryAsState()

    val list = listOf(
        BottomNavItem(
            title = "Home",
            Routes.Home.route,
            Icons.Rounded.Home
        ),
        BottomNavItem(
            title = "Profile",
            Routes.Profile.route,
            Icons.Rounded.Person
        ),
        BottomNavItem(
            title = "Post",
            Routes.Post.route,
            Icons.Rounded.Add
        ),
        BottomNavItem(
            title = "Notification",
            Routes.Notification.route,
            Icons.Rounded.Notifications
        ),
        BottomNavItem(
            title = "Search",
            Routes.Search.route,
            Icons.Rounded.Search
        )
    )

    list.forEach {
        val selected = it.route == backStackEntery?.value?.destination?.route

        NavigationBarItem(selected = selected, onClick = {
            navController1.navigate(it.route){
                popUpTo(navController1.graph.startDestinationId().id){
                    saveState = true
                }
                launchSingleTop = true
            }
        },icon = {
            Icon(imageVector = it.icon, contentDescription = it.title)
        })
    }
}