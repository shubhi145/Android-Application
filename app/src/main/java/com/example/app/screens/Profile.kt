package com.example.app.screens

import android.content.SharedPreferences
import android.content.pm.PackageManager
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.app.R
import com.example.app.itemView.PostItem
import com.example.app.model.UserModel
import com.example.app.navigation.Routes
import com.example.app.utils.Share
import com.example.app.viewmodel.AuthViewModel
import com.example.app.viewmodel.UserViewModel
import com.google.firebase.auth.FirebaseAuth

@Composable

fun Profile(navHostController: NavController) {

    val context = LocalContext.current

    val viewModel: AuthViewModel = viewModel()
    val firebaseUser by viewModel.firebaseUser.observeAsState(null)


    val userViewModel: UserViewModel = viewModel()
    val posts by userViewModel.post.observeAsState(null)

    val followerList by userViewModel.followerList.observeAsState(null)
    val followingList by userViewModel.followingList.observeAsState(null)

    var currentUserId = ""

    if (FirebaseAuth.getInstance().currentUser != null) {
        currentUserId = FirebaseAuth.getInstance().currentUser!!.uid
    }

    if (currentUserId != null) {
        userViewModel.getFollowing(currentUserId)
        userViewModel.getFollowers(currentUserId)
    }
    if (firebaseUser != null) {
        userViewModel.fetchUser(firebaseUser!!.uid)
    }

    val user = UserModel(
        name = Share.getName(context),
        imageUrl = Share.getImageUrl(context),
        username = Share.getUserName(context),

        )
    LaunchedEffect(firebaseUser) {
        if (firebaseUser == null) {
            navHostController.navigate(Routes.Login.route) {
                popUpTo(navHostController.graph.startDestinationId)
                launchSingleTop = true

            }

        }
    }


    LazyColumn {
        item {
            ConstraintLayout(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                val (button, text, logo, userName, following, followers, bio, follower) = createRefs()



                Text(
                    text = Share.getName(context),
                    style = androidx.compose.ui.text.TextStyle(
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 24.sp
                    ),
                    modifier = Modifier.constrainAs(text) {
                        start.linkTo(parent.start)
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                    }
                )

                Image(
                    painter = rememberAsyncImagePainter(model = Share.getImageUrl(context)),
                    contentDescription = "Profile Image",
                    modifier = Modifier
                        .constrainAs(logo) {
                            top.linkTo(parent.top)
                            start.linkTo(parent.end)
                        }
                        .size(120.dp)
                        .clip(CircleShape)
                )

                Text(
                    text = Share.getUserName(context),
                        style = androidx.compose.ui.text.TextStyle(
                            fontSize = 24.sp
                        ),
                        modifier = Modifier.constrainAs(userName) {
                            start.linkTo(parent.start)
                            top.linkTo(text.bottom)

                        }
                    )



                Text(
                    text = Share.getBio(context),
                        style = androidx.compose.ui.text.TextStyle(
                            fontSize = 24.sp
                        ),
                        modifier = Modifier.constrainAs(bio) {
                            start.linkTo(parent.start)
                            top.linkTo(userName.bottom)

                        }
                    )



                Text(
                    text = "${followerList?.size} Followers",
                    style = androidx.compose.ui.text.TextStyle(
                        fontSize = 24.sp
                    ),
                    modifier = Modifier.constrainAs(followers) {
                        start.linkTo(parent.start)
                        top.linkTo(bio.bottom)

                    }
                )

                Text(
                    text = "${followingList?.size}Following",
                    style = androidx.compose.ui.text.TextStyle(
                        fontSize = 24.sp
                    ),
                    modifier = Modifier.constrainAs(following) {
                        start.linkTo(parent.start)
                        top.linkTo(follower.bottom)

                    }
                )


                ElevatedButton(onClick = {
                    viewModel.logout()
                }, modifier = Modifier.constrainAs(button) {
                    top.linkTo(following.bottom)
                    start.linkTo(parent.start)
                }) {
                    Text(text = "Logout")
                }

            }
        }

        items(posts ?: emptyList()) { pairs ->
            PostItem(
                post = pairs,
                user = user,
                navHostController,
                FirebaseAuth.getInstance().currentUser!!.uid
            )
        }
    }
}