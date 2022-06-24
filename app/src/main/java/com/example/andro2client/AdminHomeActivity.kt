package com.example.andro2client



import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
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

    Button(
        modifier = Modifier.padding(top = 16.dp),
        onClick = {

            context.startActivity(Intent(context, RecipeAdminActivity::class.java))

        }) {
        Text("Manage Recipes")
    }

    Button(
        modifier = Modifier.padding(top = 16.dp),
        onClick = {

            context.startActivity(Intent(context, UserAdminActivity::class.java))

        }) {
        Text("Manage Users")
    }

    Button(
        modifier = Modifier.padding(top = 16.dp),
        onClick = {
            deleteDBM(context)
            context.startActivity(Intent(context, MainActivity::class.java))

        }) {
        Text("logout")
    }
}