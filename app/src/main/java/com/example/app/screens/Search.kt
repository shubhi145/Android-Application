package com.example.app.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.app.itemView.UserItem
import com.example.app.viewmodel.SearchViewModel


@Composable
fun Search(navHostController: NavHostController) {

    var search by remember {
        mutableStateOf("")
    }

    val searchViewModel: SearchViewModel = viewModel()
    val userList by searchViewModel.userList.observeAsState(emptyList())

    Column {
        Text(
            text = "Search",
            style = androidx.compose.ui.text.TextStyle(
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            ),
            modifier = Modifier.padding(top = 16.dp, start = 16.dp)
        )

        OutlinedTextField(
            value = search,
            onValueChange = { search = it },
            label = { Text(text = "Search User") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text
            ),
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            leadingIcon = {
                Icon(imageVector = Icons.Default.Search, contentDescription = null)
            }
        )

        Spacer(modifier = Modifier.height(20.dp))

        LazyColumn {
            val filteredItems = userList.filter {
                it.name.contains(search, ignoreCase = true) || it.username.contains(search, ignoreCase = true)
            }
            items(filteredItems) { user ->
                UserItem(user = user, navHostController = navHostController)
            }
        }
    }
}
