package com.example.andro2client

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.andro2client.model.LoginUser
import com.example.andro2client.ui.theme.Andro2ClientTheme
import com.example.andro2client.ui.theme.MainScreen
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import com.fasterxml.jackson.module.kotlin.readValue
import org.json.JSONArray
import org.json.JSONObject
import com.example.andro2client.model.Recipe
import com.example.andro2client.model.User



@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun BookScreenView(modifier: Modifier = Modifier) {

    val listdatastate= MyRecipeList()
    bookView(listdatastate)
}

@Composable
private fun MyRecipeList(): ArrayList<Recipe>? {
    val listdata = ArrayList<Recipe>()
    val listdatastate = remember {
        mutableStateOf<ArrayList<Recipe>?>(null)
    }

    compositeDisposable.add(myService.getMyRecipe(LoginUser.loginEmail)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe { result ->

            val answer = JSONArray(result)

            for (i in 0 until answer.length()) {
                var json_objectdetail: JSONObject =answer.getJSONObject(i)
                var model:Recipe= Recipe(
                    json_objectdetail.getString("_id"),
                    json_objectdetail.getString("time"),
                    json_objectdetail.getString("level"),
                    json_objectdetail.getString("type"),
                    json_objectdetail.getString("creatorEmail"),
                );
                listdata.add(model)
            }
            listdatastate.value = listdata
        }
    )
    return listdatastate.value
}

@Composable
fun bookView(listdata: ArrayList<Recipe>?){
    val context = LocalContext.current


    if(listdata!=null){

        LazyColumn {

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
                            text = listdata.get(it).level + " | " + listdata.get(it).time,
                            modifier = Modifier.padding(8.dp)

                        )
                    }
                }
            }
        }
    }
}



