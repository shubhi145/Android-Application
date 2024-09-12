package com.example.app.screens

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.app.itemView.PostItem
import com.example.app.viewmodel.HomeViewModel
import com.google.firebase.auth.FirebaseAuth

@Composable
fun Home(navController: NavController) {
    val context = LocalContext.current

    val homeViewModel: HomeViewModel = viewModel()
    val postAndUser by homeViewModel.postanduser.observeAsState(null)

    LazyColumn {
        items(postAndUser ?: emptyList()) { pairs ->
            PostItem(
                post = pairs.first,
                user = pairs.second,
                navHostController = navController,
                userId = FirebaseAuth.getInstance().currentUser!!.uid
            )
        }
    }
}
