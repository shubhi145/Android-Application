package com.example.app.itemView

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.app.model.PostModel
import com.example.app.model.UserModel

@Composable
fun PostItem(
    post: PostModel,
    user: UserModel,
    navHostController: NavController,
    userId: String
) {
    Column {
        ConstraintLayout(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            val (logo, username, title, userImage) = createRefs()

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
                text = post.post,
                style = androidx.compose.ui.text.TextStyle(
                    fontSize = 18.sp
                ),
                modifier = Modifier.constrainAs(title) {
                    start.linkTo(username.start)
                    top.linkTo(username.bottom, margin = 8.dp)
                }
            )

            if (post.image.isNotEmpty()) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .constrainAs(userImage) {
                            top.linkTo(title.bottom, margin = 8.dp)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        }
                ) {
                    Image(
                        painter = rememberAsyncImagePainter(model = post.image),
                        contentDescription = "Selected Image",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                }
            }
        }

        Divider(color = Color.LightGray, thickness = 1.dp)
    }
}
