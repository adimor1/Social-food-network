package com.example.andro2client.Admin



import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Bookmark
import androidx.compose.material.icons.rounded.Logout
import androidx.compose.material.icons.rounded.ManageAccounts
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.ImagePainter
import coil.compose.rememberImagePainter
import com.example.andro2client.MainActivity
import com.example.andro2client.R
import com.example.andro2client.deleteDBM
import com.example.andro2client.ui.theme.Andro2ClientTheme



class AdminHomeActivity: ComponentActivity() {

    @ExperimentalFoundationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Andro2ClientTheme {
                AdminHomeView()

            }
        }
    }
}

@Composable
fun AdminHomeView(){
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(30.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(modifier = Modifier
            .height(200.dp)
            .width(250.dp),


            contentAlignment = Alignment.Center
        ){

            val painter1= rememberImagePainter(
                data="https://firebasestorage.googleapis.com/v0/b/andro2client.appspot.com/o/app%2Flogo.jpg?alt=media&token=8f9e729a-2d62-4578-9785-253a0238ed9a",
                builder = {
                    error(R.drawable.error2)
                }
            )
            val printState = painter1.state
            Image(

                painter = painter1,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.size(300.dp)

            )

            if (printState is ImagePainter.State.Loading){
                CircularProgressIndicator()
            }
        }

        Text("Welcome to the manager app!",  fontSize = 25.sp)
        Spacer(modifier = Modifier.size(16.dp))

        Row() {
            Button(
                modifier = Modifier.padding(top = 16.dp),

                onClick = {
                    context.startActivity(Intent(context, RecipeAdminActivity::class.java))
                }, colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color.DarkGray,
                    contentColor = Color.White)) {

                Text("Manage Recipes")
            }
            Spacer(modifier = Modifier.size(16.dp))
            Button(
                modifier = Modifier.padding(top = 16.dp),
                onClick = {
                    context.startActivity(Intent(context, UserAdminActivity::class.java))
                }
            , colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color.DarkGray,
                    contentColor = Color.White)
            ) {

                Text("Manage Users")
            }
        }
        Spacer(modifier = Modifier.size(250.dp))

        Button(
            modifier = Modifier.padding(top = 16.dp),
            onClick = {
                deleteDBM(context)
                context.startActivity(Intent(context, MainActivity::class.java))

            }) {
            Icon(
                Icons.Rounded.Logout,
                contentDescription = "Localized description",
                tint = Color.White,
            )
            Text("logout")
        }
    }
}