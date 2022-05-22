package com.example.andro2client

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.andro2client.model.LoginUser
import com.example.andro2client.model.User
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.json.JSONObject


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ProfileView(modifier: Modifier = Modifier) {

    ProfileDetails()
    profileView()
}

private fun ProfileDetails() {
    compositeDisposable.add(myService.getUserDetails(LoginUser.loginEmail)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe { result ->

            val answer = JSONObject(result)

            var model: User = User(answer.getString("email"), answer.getString("name"));
           // profileView()
        }
    )
}

@Composable
fun profileView(modifier: Modifier = Modifier){
    Column(
        modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "My Profile"
        )
    }
}