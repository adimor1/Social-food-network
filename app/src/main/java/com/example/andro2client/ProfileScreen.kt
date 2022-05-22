package com.example.andro2client

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.andro2client.model.LoginUser
import com.example.andro2client.model.Recipe
import com.example.andro2client.model.User
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.json.JSONObject
import org.json.JSONArray

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ProfileView(modifier: Modifier = Modifier) {
    val dddd = ProfileDetails()
    ProfileView(dddd)
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


            user.value= User(objectAnswer.getString("email"), objectAnswer.getString("name"));

        }

    )
    return user.value
}

@Composable
fun ProfileView(user: User?){

    if(user!=null){
        Column(
            modifier= Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = user.email
            )
        }
    }

}