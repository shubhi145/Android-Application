package com.example.app.screens

import android.content.SharedPreferences
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.app.itemView.PostItem
import com.example.app.model.UserModel
import com.example.app.navigation.Routes
import com.example.app.utils.Share
import com.example.app.viewmodel.AuthViewModel
import com.example.app.viewmodel.UserViewModel
import com.google.firebase.auth.FirebaseAuth

@Composable
fun Otheruser(navHostController: NavHostController, uid: String) {

    val context = LocalContext.current

    val viewModel: AuthViewModel = viewModel()
    val firebaseUser by viewModel.firebaseUser.observeAsState(null)


    val userViewModel: UserViewModel = viewModel()
    val posts by userViewModel.post.observeAsState(null)
    val users by userViewModel.user.observeAsState(null)
    val followerList by userViewModel.followerList.observeAsState(null)
    val followingList by userViewModel.followingList.observeAsState(null)


    userViewModel.fetchUser(uid)
    userViewModel.fetchUser(uid)
    userViewModel.getFollowing(uid)
    userViewModel.getFollowers(uid)

    var currentUserId = ""

    if (FirebaseAuth.getInstance().currentUser != null) {
        currentUserId = FirebaseAuth.getInstance().currentUser!!.uid
    }
//
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
                    text = users!!.name,
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
                    painter = rememberAsyncImagePainter(model = users!!.imageUrl),
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
                    text = users!!.username,
                    style = androidx.compose.ui.text.TextStyle(
                        fontSize = 24.sp
                    ),
                    modifier = Modifier.constrainAs(userName) {
                        start.linkTo(parent.start)
                        top.linkTo(text.bottom)

                    }
                )



                Text(
                    text = users!!.bio,
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
                    if (currentUserId != "") {

                        if (FirebaseAuth.getInstance().currentUser != null) {
                            userViewModel.followuser(uid, currentUserId)
                        }

                    }
                }, modifier = Modifier.constrainAs(button) {
                    top.linkTo(following.bottom)
                    start.linkTo(parent.start)
                }) {
                    Text(
                        text = if (followerList != null && followerList!!.isNotEmpty() && followerList!!.contains(
                                currentUserId
                            )
                        ) "Following" else "Follow"
                    )
                }

            }
        }

        if (posts != null && users != null) {
            items(posts ?: emptyList()) { pairs ->
                PostItem(
                    post = pairs,
                    user = users!!,
                    navHostController,
                    userId = Share.getUserName(context)
                )
            }
        }

    }
}