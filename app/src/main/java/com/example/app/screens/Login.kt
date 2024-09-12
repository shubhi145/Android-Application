package com.example.app.screens

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.app.navigation.Routes
import com.example.app.viewmodel.AuthViewModel
import java.lang.reflect.Modifier

@RequiresApi(Build.VERSION_CODES.O)
@Composable

fun Login(navController: NavHostController) {

    val AuthviewModel: AuthViewModel = viewModel()
    val firebaseUser by AuthviewModel.firebaseUser.observeAsState(null)
    val error by AuthviewModel.error.observeAsState("")
    val context = LocalContext.current
    var email by remember {
        mutableStateOf("")
    }

    var password by remember {
        mutableStateOf("")
    }

    LaunchedEffect(firebaseUser) {
        if (firebaseUser != null) {
            navController.navigate(Routes.BottomNavigation.route) {
                popUpTo(navController.graph.startDestinationId)
                launchSingleTop = true

            }

        }
    }

    LaunchedEffect(error) {
        if (firebaseUser != null) {
            navController.navigate(Routes.BottomNavigation.route) {
                popUpTo(navController.graph.startDestinationId)
                launchSingleTop = true

            }

        }
    }

    error?.let {
        Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
    }
    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Alignment.Center
    ) {

        Text(
            text = "Login", style = androidx.compose.ui.text.TextStyle(
                fontWeight = FontWeight.ExtraBold,
                fontSize = 24.sp
            )
        )

        Box(modifier = Modifier.height(50.dp))
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text(text = "Email") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email
            ), singleLine = true, modifier = Modifier.fillMaxWidth()
        )

        Box(modifier = Modifier.height(20.dp))
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text(text = "Password") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password
            ), singleLine = true, modifier = Modifier.fillMaxWidth()
        )

        Box(modifier = Modifier.height(30.dp))

        ElevatedButton(onClick = {

            if(email.isEmpty() || password.isEmpty()){
                Toast.makeText(context, "Please enter your email", Toast.LENGTH_SHORT).show()

            }else {
                AuthviewModel.login(email, password, context)
            }
        }, modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "Login", style = androidx.compose.ui.text.TextStyle(
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 24.sp
                ), modifier = Modifier.padding(vertical = 6.dp)
            )
        }

        TextButton(onClick = {
            navController.navigate(Routes.Register.route) {
                popUpTo(navController.graph.startDestinationId)
                launchSingleTop = true
            }

        }, modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "New User? Create New Account", style = androidx.compose.ui.text.TextStyle(
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 16.sp
                )
            )
        }
    }
}