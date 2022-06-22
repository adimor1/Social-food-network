package com.example.andro2client

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.annotation.ExperimentalCoilApi
import coil.compose.ImagePainter
import coil.compose.rememberImagePainter
import com.example.andro2client.model.LoginUser
import com.example.andro2client.ui.theme.Andro2ClientTheme
import com.example.andro2client.ui.theme.MainScreen
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.json.JSONArray
import org.json.JSONObject
import com.example.andro2client.model.Recipe
import com.example.andro2client.model.RecipeUser
import com.example.andro2client.model.User


class HomeActivity: ComponentActivity() {

    @ExperimentalFoundationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Andro2ClientTheme {
                MainScreen()

            }
        }
    }
}

@ExperimentalFoundationApi
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun HomeScreenView(modifier: Modifier = Modifier) {

    val listdatastate= RecipeList()

    HomeView(listdatastate, Favorite())

}

@Composable
private fun Favorite() :String{

    val favoriteState = remember { mutableStateOf("") }

    compositeDisposable.add(myService.getUserDetails(LoginUser.loginEmail)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe { result ->

            val answer = JSONArray(result)
            val objectAnswer: JSONObject =answer.getJSONObject(0)

             favoriteState.value = objectAnswer.getString("favorite")
        }

    )
    return favoriteState.value
}

@Composable
private fun RecipeList(): ArrayList<RecipeUser>? {
    val mylistdata = ArrayList<RecipeUser>()
    val mylistdatastate = remember {
        mutableStateOf<ArrayList<RecipeUser>?>(null)
    }

    compositeDisposable.add(myService.getrecipeuser()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe { result ->

            val answer = JSONArray(result)

            for (i in 0 until answer.length()) {
                val json_objectdetail: JSONObject =answer.getJSONObject(i)
                val userrecipe: JSONArray = json_objectdetail.getJSONArray("userrecipe")
                val bluev: JSONObject =userrecipe.getJSONObject(0)
                val model:RecipeUser= RecipeUser(
                    json_objectdetail.getString("_id"),
                    json_objectdetail.getString("time"),
                    json_objectdetail.getString("level"),
                    json_objectdetail.getString("type"),
                    json_objectdetail.getString("foodType"),
                    json_objectdetail.getString("creatorEmail"),
                    json_objectdetail.getString("imageRec"),
                    bluev.getString("isBlueV")
                );

                if(model.type=="public"){
                    mylistdata.add(model)
                }


            }

            mylistdatastate.value = mylistdata
        }
    )

    return mylistdatastate.value
}



@ExperimentalFoundationApi
@Composable
fun HomeView(mylistdata: ArrayList<RecipeUser>?,  favorite: String){
    val context = LocalContext.current
    val favoritelist = ArrayList<RecipeUser>()
    val otherlist = ArrayList<RecipeUser>()

    if (mylistdata != null) {
        for (i in 0 until mylistdata.size) {

            if(mylistdata[i].foodType == favorite){
                favoritelist.add(mylistdata[i])
            }
            else{
                otherlist.add(mylistdata[i])
            }
        }
    }


if(mylistdata!=null){
    
    LazyColumn {
        stickyHeader {
            Spacer(modifier = Modifier.size(50.dp))
            Card(elevation = 4.dp,  backgroundColor =  Color(0xFF302E2E)) {

                Box(
                    Modifier
                        .fillMaxWidth()
                        .height(50.dp), Alignment.Center, ){
                    Row(){
                        Icon(Icons.Rounded.Favorite, contentDescription = "Localized description",   tint = Color.White,)
                        Spacer(modifier = Modifier.size(10.dp))
                        Text(text = "Recommended for you", fontWeight = FontWeight.Bold, color = Color.White)
                    }
                }
            }
        }

        items(favoritelist.size) {
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
                        val intent = Intent(context, RecipeActivity::class.java)
                        intent.putExtra("RECIPE_ID", favoritelist.get(it))
                        context.startActivity(intent)
                    }
                ) {
                    ImageLoader(favoritelist.get(it).imageRec)
                    Spacer(modifier = Modifier.width(1.dp))
                    Column(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth()
                            .align(Alignment.CenterVertically)) {
                        Text(text = favoritelist.get(it).time+ " | "+ favoritelist.get(it).level)
                        Spacer(modifier = Modifier.padding(5.dp))
                        Row() {
                                    if(favoritelist.get(it).isBlueV =="true") {
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
                                Text(text = "By "+ favoritelist.get(it).creatorMail)
                        }

                    }
                }
            }
        }

        //*****OTHER-LIST*****
        stickyHeader {
            Spacer(modifier = Modifier.size(50.dp))
            Card(elevation = 4.dp,  backgroundColor =  Color(0xFF302E2E)) {

                Box(
                    Modifier
                        .fillMaxWidth()
                        .height(50.dp), Alignment.Center, ){
                    Row(){
                        Icon(Icons.Rounded.LibraryBooks, contentDescription = "Localized description",   tint = Color.White,)
                        Spacer(modifier = Modifier.size(10.dp))
                        Text(text = "More recipes", fontWeight = FontWeight.Bold, color = Color.White)
                    }
                }
            }
        }

        items(otherlist.size) {
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
                        val intent = Intent(context, RecipeActivity::class.java)
                        intent.putExtra("RECIPE_ID", otherlist.get(it))
                        context.startActivity(intent)
                    }
                ) {
                    ImageLoader(otherlist.get(it).imageRec)
                    Spacer(modifier = Modifier.width(1.dp))
                    Column(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth()
                            .align(Alignment.CenterVertically)) {
                        Text(text = otherlist.get(it).time+ " | "+ otherlist.get(it).level)
                        Spacer(modifier = Modifier.padding(5.dp))
                        Row() {
                            if(otherlist.get(it).isBlueV =="true") {
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
                            Text(text = "By "+ otherlist.get(it).creatorMail)
                        }

                    }
                }
            }
        }
    }
}
}

@ExperimentalCoilApi
@Composable
fun ImageLoader(imageRec: String){


    Box(modifier = Modifier
        .height(150.dp)
        .width(150.dp),


        contentAlignment = Alignment.Center
    ){

        val painter1= rememberImagePainter(
            data="https://firebasestorage.googleapis.com/v0/b/andro2client.appspot.com/o/"+imageRec+"?alt=media&token=125af2e1-dbde-49f8-9627-16bc938ebdb7",
            builder = {
                error(R.drawable.error2)
            }
        )
        val printState = painter1.state
        Image(

            painter = painter1,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.size(120.dp)

        )

        if (printState is ImagePainter.State.Loading){
            CircularProgressIndicator()
        }
    }

}