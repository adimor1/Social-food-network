package com.example.andro2client

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.andro2client.model.LoginUser
import com.example.andro2client.model.Recipe
import com.example.andro2client.model.User
import com.example.myapplication.delete
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.json.JSONObject
import org.json.JSONArray

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ProfileView(modifier: Modifier = Modifier) {
    val userstate = ProfileDetails()
    ProfileView(userstate)
}

@Composable
private fun ProfileDetails() :User?{
    val user = remember {
        mutableStateOf<User?>(null)
    }

    compositeDisposable.add(myService.getUserDetails(LoginUser.loginEmail)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe { result ->

            val answer = JSONArray(result)
            var objectAnswer: JSONObject =answer.getJSONObject(0)


            user.value= User(objectAnswer.getString("_id"), objectAnswer.getString("email"), objectAnswer.getString("name"));

        }

    )
    return user.value
}

@Composable
fun ProfileView(user: User?){
    val context = LocalContext.current
    if(user!=null){
        Column(
            modifier= Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = user.name
            )
            Text(
                text = user.email
            )
        }

        Button(
            modifier = Modifier.padding(top = 16.dp),
            onClick = {
                editUser(user, context)
            }) {
            Text("edit")
        }
    }

}

fun editUser(user: User, context:Context) {
    compositeDisposable.add(myService.editUser(user.id)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe { result ->
            context.startActivity(Intent(context, HomeActivity::class.java))
        }
    )
}
