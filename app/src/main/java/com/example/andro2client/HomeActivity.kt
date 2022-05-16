package com.example.andro2client

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.andro2client.model.Recipe
import com.example.andro2client.ui.theme.Andro2ClientTheme
import com.example.andro2client.ui.theme.MainScreen
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import com.fasterxml.jackson.module.kotlin.readValue


class HomeActivity: ComponentActivity() {

    @ExperimentalFoundationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            Andro2ClientTheme {
                compositeDisposable.add(myService.getRecipe()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe { result ->
                        val mapper = jacksonObjectMapper()
                        val genres: List<Recipe> = mapper.readValue(result)

                    }
                )
                MainScreen()
                HomeScreenView()
            }
        }
    }

    @Composable
    @ExperimentalFoundationApi
    private fun recipeList(result: String?) {
        LazyColumn {
            // Add a single item
            item {
                Text(text = "First item")
            }

            // Add 5 items
            items(5) { index ->
                Text(text = "Item: $index")
            }

            // Add another single item
            item {
                Text(text = "Last item")
            }
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

            Button(
                modifier = Modifier.padding(top = 16.dp),
                onClick = {
                    getRecipe()
                }) {
                Text("get recipes")
            }
        }
    }

    fun getRecipe() {
        compositeDisposable.add(myService.getRecipe()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { result ->
                print(result)
            }
        )
    }
}