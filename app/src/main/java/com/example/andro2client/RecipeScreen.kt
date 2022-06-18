package com.example.myapplication

import android.content.Context
import android.content.Intent
import android.text.TextUtils
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.andro2client.*
import com.example.andro2client.compositeDisposable
import com.example.andro2client.model.LoginUser
import com.example.andro2client.model.Recipe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

@Composable
fun RecipeScreen(recipe: Recipe){
    val scrollState = rememberScrollState()
    val context = LocalContext.current
    Column(modifier = Modifier.fillMaxSize()) {
        BoxWithConstraints() {
            Surface() {
                Column(
                    modifier= Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = recipe.level
                    )

                    if(recipe.creatorMail==LoginUser.loginEmail){
                        Button(
                            modifier = Modifier.padding(top = 16.dp),
                            onClick = {
                                delete(recipe, context)
                            }) {
                            Text("delete")
                        }
                    }
                    else{
                        Button(
                            modifier = Modifier.padding(top = 16.dp),
                            onClick = {
                                saveToMyList(recipe, context)
                            }) {
                            Text("save to my list")
                        }
                    }
                }
            }
        }
    }
}

fun saveToMyList(recipe: Recipe, context: Context) {
    compositeDisposable.add(myService.saveToMyList(recipe.id, LoginUser.loginEmail)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe { result ->

        }
    )
}

fun delete(recipe: Recipe, context: Context) {
    compositeDisposable.add(myService.deleteRecipe(recipe.id)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe { result ->
            context.startActivity(Intent(context, HomeActivity::class.java))
        }
    )
}

