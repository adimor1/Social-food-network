package com.example.andro2client.Admin

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.ImagePainter
import coil.compose.rememberImagePainter
import com.example.andro2client.R
import com.example.andro2client.compositeDisposable
import com.example.andro2client.model.User
import com.example.andro2client.myService
import com.example.andro2client.ui.theme.Andro2ClientTheme
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.json.JSONArray
import org.json.JSONObject


class UserAdminActivity: ComponentActivity() {

    @ExperimentalFoundationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Andro2ClientTheme {
                UserView(UserList())

            }
        }
    }
}

@Composable
private fun UserList(): ArrayList<User>? {
    val mylistdata = ArrayList<User>()
    val mylistdatastate = remember {
        mutableStateOf<ArrayList<User>?>(null)
    }

    compositeDisposable.add(
        myService.getUsers()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe { result ->

            val answer = JSONArray(result)

            for (i in 0 until answer.length()) {
                val json_objectdetail: JSONObject =answer.getJSONObject(i)
                val model:User= User(
                    json_objectdetail.getString("_id"),
                    json_objectdetail.getString("email"),
                    json_objectdetail.getString("name"),
                    json_objectdetail.getString("favorite"),
                    json_objectdetail.getString("birth"),
                    json_objectdetail.getString("type"),
                    json_objectdetail.getString("gender"),
                    json_objectdetail.getString("isBlueV"),
                    json_objectdetail.getString("isAdmin"),
                );
                mylistdata.add(model)
            }

            mylistdatastate.value = mylistdata
        }
    )

    return mylistdatastate.value
}

@ExperimentalFoundationApi
@Composable
fun UserView(mylistdata: ArrayList<User>?){
    val context = LocalContext.current

    if(mylistdata!=null){

        Spacer(modifier = Modifier.width(20.dp))
        LazyColumn {
            items(mylistdata.size) {

                Card(
                    modifier = Modifier
                        .padding(10.dp)
                        .fillMaxWidth()
                        .wrapContentHeight(),

                    shape = RoundedCornerShape(20.dp),
                    elevation = 8.dp,
                    backgroundColor = MaterialTheme.colors.background
                )
                {
                    Row(
                        Modifier.clickable {
                            val intent = Intent(context, ProfileAdminActivity::class.java)
                            intent.putExtra("USER_ID", mylistdata.get(it))
                            context.startActivity(intent)
                        }
                    ) {

                        Spacer(modifier = Modifier.width(1.dp))
                        Column(
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxWidth()
                                .align(Alignment.CenterVertically)
                        ) {

                            Text(
                                text = " " + mylistdata.get(it).name,
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp
                            )
                            Spacer(modifier = Modifier.padding(2.dp))


                            Row() {
                                if(mylistdata.get(it).isBlueV =="true") {
                                    Box(
                                        modifier = Modifier
                                            .height(25.dp)
                                            .width(25.dp),
                                        contentAlignment = Alignment.Center
                                    ) {

                                        val painter1 = rememberImagePainter(
                                            data = "https://firebasestorage.googleapis.com/v0/b/andro2client.appspot.com/o/app%2Ftwitter600.jpg?alt=media&token=95fd022b-62ee-4925-be05-2f4507450da0",
                                            builder = {
                                                error(R.drawable.error2)
                                            }
                                        )
                                        val printState = painter1.state
                                        Image(

                                            painter = painter1,
                                            contentDescription = null,
                                            contentScale = ContentScale.Crop,
                                            modifier = Modifier.size(20.dp)
                                        )
                                        if (printState is ImagePainter.State.Loading) {
                                            CircularProgressIndicator()
                                        }
                                    }
                                }
                                Text(text = "By "+ mylistdata.get(it).email)
                            }

                        }
                    }
                }
            }
        }
    }
}
