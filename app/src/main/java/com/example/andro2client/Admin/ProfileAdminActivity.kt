package com.example.andro2client.Admin

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.andro2client.model.User
import com.example.andro2client.ui.theme.Andro2ClientTheme

class ProfileAdminActivity : ComponentActivity() {

    private val user:User by lazy {
        intent?.getSerializableExtra("USER_ID") as User
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent{
            Andro2ClientTheme {
                UserScreen(user = user)
            }
        }
    }

}