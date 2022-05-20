package com.example.andro2client

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
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

val listdata = ArrayList<Recipe>()

class HomeActivity: ComponentActivity() {

    @ExperimentalFoundationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        RecipeList()
    }

    fun viewHome(){
        setContent {
            Andro2ClientTheme {

                MainScreen()
                HomeScreenView()
                LazyColumn {

                        items(listdata.size) {
                            Text(
                                text = listdata.get(it).level + " | " + listdata.get(it).time,
                                modifier = Modifier.padding(8.dp)
                            )

                        }
                    
                }
            }
        }
    }


    private fun RecipeList() {

        compositeDisposable.add(myService.getRecipe()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { result ->

                val answer = JSONArray(result)

                    for (i in 0 until answer.length()) {
                        var json_objectdetail: JSONObject =answer.getJSONObject(i)
                        var model:Recipe= Recipe();
                        model.time=json_objectdetail.getString("time")
                        model.level=json_objectdetail.getString("level")
                        listdata.add(model)
                    }
                viewHome()
            }
        )
    }
}

    @OptIn(ExperimentalComposeUiApi::class)
    @Composable
    fun HomeScreenView(modifier: Modifier = Modifier) {

        Column(
            modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {


        }
    }


