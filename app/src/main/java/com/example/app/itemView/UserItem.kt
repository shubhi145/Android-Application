package com.example.app.itemView

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.app.model.UserModel
import com.example.app.navigation.Routes

@Composable
fun UserItem(
    user: UserModel,
    navHostController: NavHostController
) {
    Column {
        ConstraintLayout(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp).clickable {
                    val routes = Routes.OtherUser.route.replace("{data}", user.uid)
                    navHostController.navigate(routes)
                }
        ) {
            val (logo, username, name) = createRefs()

            Image(
                painter = rememberAsyncImagePainter(model = user.imageUrl),
                contentDescription = "Profile Image",
                modifier = Modifier
                    .constrainAs(logo) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                    }
                    .size(40.dp)
                    .clip(CircleShape)
            )

            Text(
                text = user.username,
                style = androidx.compose.ui.text.TextStyle(
                    fontSize = 24.sp
                ),
                modifier = Modifier.constrainAs(username) {
                    start.linkTo(logo.end, margin = 12.dp)
                    top.linkTo(logo.top)
                    bottom.linkTo(logo.bottom)
                }
            )

            Text(
                text = user.name,
                style = androidx.compose.ui.text.TextStyle(
                    fontSize = 18.sp
                ),
                modifier = Modifier.constrainAs(name) {
                    start.linkTo(username.start)
                    top.linkTo(username.bottom, margin = 2.dp)
                }
            )
        }

        Divider(color = Color.LightGray, thickness = 1.dp)
    }
}
