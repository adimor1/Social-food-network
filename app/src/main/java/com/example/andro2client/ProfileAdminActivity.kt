package com.example.andro2client

import android.content.Context
import android.content.Intent

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.Text
import com.example.andro2client.model.Recipe
import com.example.andro2client.model.RecipeUser
import com.example.andro2client.model.User
import com.example.andro2client.ui.theme.Andro2ClientTheme
import com.example.myapplication.RecipeScreen

class ProfileAdminActivity : ComponentActivity() {

    private val user:User by lazy {
        intent?.getSerializableExtra("USER_ID") as User
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent{
            Andro2ClientTheme {

            }
        }
    }

}