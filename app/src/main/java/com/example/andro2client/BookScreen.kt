package com.example.andro2client

import android.content.Intent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.andro2client.model.LoginUser
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.json.JSONArray
import org.json.JSONObject
import com.example.andro2client.model.Recipe
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import com.example.andro2client.model.RecipeUser

@ExperimentalFoundationApi
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun BookScreenView(modifier: Modifier = Modifier) {

    val listdatastate= MyRecipeList()
    val listdatastate2= MySavedList()

    bookView(listdatastate, listdatastate2)
}



@Composable
private fun MyRecipeList(): ArrayList<RecipeUser>? {
    val listdata = ArrayList<RecipeUser>()
    val listdatastate = remember {
        mutableStateOf<ArrayList<RecipeUser>?>(null)
    }

    compositeDisposable.add(myService.getMyRecipe(LoginUser.loginEmail)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe { result ->

            val answer = JSONArray(result)

            for (i in 0 until answer.length()) {
                var json_objectdetail: JSONObject =answer.getJSONObject(i)
               // val userrecipe: JSONArray = json_objectdetail.getJSONArray("userrecipe")
               // val bluev: JSONObject =userrecipe.getJSONObject(0)
                var model:RecipeUser= RecipeUser(
                    json_objectdetail.getString("_id"),
                    json_objectdetail.getString("time"),
                    json_objectdetail.getString("level"),
                    json_objectdetail.getString("type"),
                    json_objectdetail.getString("foodType"),
                    json_objectdetail.getString("creatorEmail"),
                    json_objectdetail.getString("imageRec"),
                    json_objectdetail.getString("name"),
                    json_objectdetail.getString("ingredients"),
                    json_objectdetail.getString("instruction"),
                    json_objectdetail.getString("sponsored"),
                   ""
                );
                listdata.add(model)
            }
            listdatastate.value = listdata
        }
    )




    return listdatastate.value
}



@Composable
private fun MySavedList(): ArrayList<RecipeUser>? {
    val listdata2 = ArrayList<RecipeUser>()
    val listdatastate2 = remember {
        mutableStateOf<ArrayList<RecipeUser>?>(null)
    }

    compositeDisposable.add(myService.getRecipeById(LoginUser.loginEmail)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe { result ->

            val answer = JSONArray(result)

            for (i in 0 until answer.length()) {
                var json_objectdetail: JSONObject =answer.getJSONObject(i)
               // val userrecipe: JSONArray = json_objectdetail.getJSONArray("userrecipe")
              //  val bluev: JSONObject =userrecipe.getJSONObject(0)
                var model:RecipeUser= RecipeUser(
                    json_objectdetail.getString("_id"),
                    json_objectdetail.getString("time"),
                    json_objectdetail.getString("level"),
                    json_objectdetail.getString("type"),
                    json_objectdetail.getString("foodType"),
                    json_objectdetail.getString("creatorEmail"),
                    json_objectdetail.getString("imageRec"),
                    json_objectdetail.getString("name"),
                    json_objectdetail.getString("ingredients"),
                    json_objectdetail.getString("instruction"),
                    json_objectdetail.getString("sponsored"),
                  ""
                );
                listdata2.add(model)
            }
            listdatastate2.value = listdata2
        }
    )


    return listdatastate2.value
}

@ExperimentalFoundationApi
@Composable
fun bookView(listdata: ArrayList<RecipeUser>?, listdata2: ArrayList<RecipeUser>?){
    val context = LocalContext.current



    if(listdata!=null&&listdata2!=null){


        LazyColumn {


            stickyHeader {
                Card(elevation = 4.dp,  backgroundColor =  Color(0xFF302E2E)) {

                    Box(
                        Modifier
                            .fillMaxWidth()
                            .height(50.dp), Alignment.Center, ){
                        Row(){
                            Icon(Icons.Rounded.List, contentDescription = "Localized description",   tint = Color.White,)
                            Text(text = "Your Recipes", fontWeight = FontWeight.Bold, color = Color.White)
                        }
                    }
                }
            }

            items(listdata.size) {
                Card(
                    modifier = Modifier
                        .padding(10.dp)
                        .fillMaxWidth()
                        .wrapContentHeight(),
                    shape = MaterialTheme.shapes.medium,
                    elevation = 5.dp,
                    backgroundColor = MaterialTheme.colors.background
                )
                {
                    Row(

                        Modifier.clickable {
                            val intent = Intent(context, RecipeActivity::class.java)
                            intent.putExtra("RECIPE_ID", listdata.get(it))
                            context.startActivity(intent)
                        }
                    ) {
                        Text(
                            text = listdata.get(it).name + " | " + listdata.get(it).foodType,
                            modifier = Modifier.padding(8.dp)

                        )
                    }
                }

            }


            stickyHeader {
                Spacer(modifier = Modifier.size(50.dp))
                Card(elevation = 4.dp,  backgroundColor =  Color(0xFF302E2E)) {

                    Box(
                        Modifier
                            .fillMaxWidth()
                            .height(50.dp), Alignment.Center, ){
                        Row(){
                            Icon(Icons.Rounded.Bookmark, contentDescription = "Localized description",   tint = Color.White,)
                            Spacer(modifier = Modifier.size(10.dp))
                            Text(text = "Saved Recipes", fontWeight = FontWeight.Bold, color = Color.White)
                        }
                    }
                }
            }

            items(listdata2.size) {
                Card(
                    modifier = Modifier
                        .padding(10.dp)
                        .fillMaxWidth()
                        .wrapContentHeight(),
                    shape = MaterialTheme.shapes.medium,
                    elevation = 5.dp,
                    backgroundColor = MaterialTheme.colors.background
                )
                {
                    Row(

                        Modifier.clickable {
                            val intent = Intent(context, RecipeActivity::class.java)
                            intent.putExtra("RECIPE_ID", listdata2.get(it))
                            context.startActivity(intent)
                        }
                    ) {
                        Text(
                            text = listdata2.get(it).name + " | " + listdata2.get(it).foodType,
                            modifier = Modifier.padding(8.dp)

                        )
                    }
                }
            }
        }
    }

}



