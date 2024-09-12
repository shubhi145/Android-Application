package com.example.app.screens

import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
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
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.app.R
import com.example.app.navigation.Routes
import com.example.app.utils.Share
import com.example.app.viewmodel.PostViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.isActive
import java.time.format.TextStyle

@Composable
fun Post(navHostController: NavHostController) {
    
    val postViewModel :PostViewModel = viewModel()
    val isPoste by  postViewModel.isPosted.observeAsState(false)
    var post by remember { mutableStateOf("") }
    var imageUri by remember { mutableStateOf<Uri?>(null) }

    val context = LocalContext.current
    val permissionToReq = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        android.Manifest.permission.READ_MEDIA_IMAGES
    } else {
        android.Manifest.permission.READ_EXTERNAL_STORAGE
    }
    val launcher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri: Uri? ->
            imageUri = uri
        }
    val permissionLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                launcher.launch("image/*")
            } else {
                Toast.makeText(context, "Permission denied", Toast.LENGTH_SHORT).show()
            }
        }

    LaunchedEffect(isPoste) {
        if(isPoste!!){
            post = ""
            imageUri = null
            Toast.makeText(context, "Post Added", Toast.LENGTH_SHORT).show()
            navHostController.navigate(Routes.Home.route){
                popUpTo(Routes.Post.route){
                    inclusive = true
                }
            }
        }
    }


    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        val (crosPic, text, logo, username, editText, addMedias, imageBox,button) = createRefs()

        Image(
            painter = painterResource(id = R.drawable.baseline_close_24),
            contentDescription = "close",
            modifier = Modifier
                .constrainAs(crosPic) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                }
                .clickable {
                    navHostController.navigate(Routes.Post.route){
                        popUpTo(Routes.Post.route){
                            inclusive = true
                        }
                    }
                }
        )

        Text(
            text = "Add Post",
            style = androidx.compose.ui.text.TextStyle(
                fontWeight = FontWeight.ExtraBold,
                fontSize = 24.sp
            ),
            modifier = Modifier.constrainAs(text) {
                start.linkTo(crosPic.end, margin = 12.dp)
                top.linkTo(crosPic.top)
                bottom.linkTo(crosPic.bottom)
            }
        )

        Image(
            painter = rememberAsyncImagePainter(model =  Share.getImageUrl(context)),
            contentDescription = "Profile Image",
            modifier = Modifier
                .constrainAs(logo) {
                    top.linkTo(text.bottom, margin = 16.dp)
                    start.linkTo(parent.start)
                }
                .size(40.dp)
                .clip(CircleShape)
        )

        Text(
            text = "Username",
            style = androidx.compose.ui.text.TextStyle(
                fontSize = 24.sp
            ),
            modifier = Modifier.constrainAs(username) {
                start.linkTo(logo.end, margin = 12.dp)
                top.linkTo(logo.top)
                bottom.linkTo(logo.bottom)
            }
        )

        BasicTextBackground(
            hint = "Start a Post....",
            value = post,
            onValueChange = { post = it },
            modifier = Modifier
                .constrainAs(editText) {
                    top.linkTo(username.bottom, margin = 12.dp)
                    start.linkTo(username.start)
                    end.linkTo(parent.end)
                }
                .padding(horizontal = 8.dp, vertical = 8.dp)
                .fillMaxWidth()
        )

        if (imageUri == null) {
            Image(
                painter = painterResource(id = R.drawable.baseline_add_box_24),
                contentDescription = "Add Photo",
                modifier = Modifier
                    .constrainAs(addMedias) {
                        top.linkTo(editText.bottom, margin = 12.dp)
                        start.linkTo(editText.start)
                    }
                    .clickable {
                        val isGranted = ContextCompat.checkSelfPermission(
                            context, permissionToReq
                        ) == PackageManager.PERMISSION_GRANTED
                        if (isGranted) {
                            launcher.launch("image/*")
                        } else {
                            permissionLauncher.launch(permissionToReq)
                        }
                    }
            )
        } else {
            Box(
                modifier = Modifier
                    .background(Color.Gray)
                    .padding(1.dp)
                    .constrainAs(imageBox) {
                        top.linkTo(editText.bottom)
                        start.linkTo(editText.start)
                        end.linkTo(editText.end)
                    }
                    .height(250.dp)
            ) {
                Image(
                    painter = rememberAsyncImagePainter(model = imageUri),
                    contentDescription = "Selected Image",
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )

                androidx.compose.material3.Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "close",
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .clickable {
                            imageUri = null
                        }
                )
            }
        }

        Text(
            text = "AnyOne Can Reply",
            style = androidx.compose.ui.text.TextStyle(
                fontSize = 24.sp
            ),
            modifier = Modifier.constrainAs(username) {
                start.linkTo(parent.start, margin = 12.dp)
                bottom.linkTo(parent.bottom, margin = 12.dp)
            }
        )

        TextButton(onClick = {

            if(imageUri == null){
                postViewModel.saveData(post,FirebaseAuth.getInstance().currentUser!!.uid,"")
            }else{
                postViewModel.saveData(post,FirebaseAuth.getInstance().currentUser!!.uid,imageUri!!.toString())
            }
        },
            modifier = Modifier.constrainAs(button) {
                end.linkTo(parent.end,margin = 12.dp)
                bottom.linkTo(parent.bottom, margin = 12.dp)
            }) {
            Text(
                text = "Post",
                style = androidx.compose.ui.text.TextStyle(
                    fontSize = 20.sp
                )
            )
        }
    }
}

@Composable
fun BasicTextBackground(
    hint: String,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier
) {
    Box(modifier = modifier) {
        if (value.isEmpty()) {
            Text(text = hint, color = Color.Gray)
        }

        androidx.compose.foundation.text.BasicTextField(
            value = value,
            onValueChange = onValueChange,
            textStyle = androidx.compose.ui.text.TextStyle(color = Color.Black),
            modifier = Modifier.fillMaxWidth()
        )
    }
}
