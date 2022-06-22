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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Bookmark
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.ImagePainter
import coil.compose.rememberImagePainter
import com.example.andro2client.*
import com.example.andro2client.R
import com.example.andro2client.compositeDisposable
import com.example.andro2client.model.LoginUser
import com.example.andro2client.model.Recipe
import com.example.andro2client.model.RecipeUser
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

@Composable
fun RecipeScreen(recipe: RecipeUser){
    val scrollState = rememberScrollState()
    val context = LocalContext.current
    var spon =""
    if (recipe.sponsored=="true"){
        spon = "* This is a sponsored recipe"
    }
    Column(modifier = Modifier.fillMaxSize()) {
        BoxWithConstraints() {
            Surface() {
                Column() {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(1.dp),
                        verticalArrangement = Arrangement.spacedBy(1.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        ImageLoader(recipe.imageRec)

                        Text(
                            text = recipe.name+ " (" + recipe.foodType+ ")",
                            fontWeight = FontWeight.Bold,
                            fontSize = 25.sp

                        )


                    }

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        horizontalAlignment = Alignment.Start
                    ) {
                        Text(
                            text = recipe.level
                        )

                        Text(
                            text = recipe.time
                        )
                        Spacer(modifier = Modifier.size(25.dp))
                        Text(
                            text= "Ingredients:",
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp
                        )

                        Text(
                            text = recipe.ingredients
                        )
                        Spacer(modifier = Modifier.size(25.dp))
                        Text(
                            text= "Instruction:",
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp
                        )

                        Text(
                            text = recipe.instruction
                        )

                        Spacer(modifier = Modifier.size(25.dp))

                        Row() {

                            if (recipe.isBlueV == "true") {
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

                            Text(
                                text = "By " + recipe.creatorMail
                            )
                        }
                        Text(

                            text = spon
                        )

                        if (recipe.creatorMail == LoginUser.loginEmail) {
                            Button(
                                modifier = Modifier.padding(top = 16.dp),
                                onClick = {
                                    delete(recipe, context)
                                }) {
                                Text("delete")
                            }
                        } else {
                            Button(
                                modifier = Modifier.padding(top = 16.dp),
                                onClick = {
                                    saveToMyList(recipe, context)
                                }) {
                                Icon(
                                    Icons.Rounded.Bookmark,
                                    contentDescription = "Localized description",
                                    tint = Color.White,
                                )
                                Text("save to my list")
                            }
                        }
                    }
                }
            }
        }
    }
}

fun saveToMyList(recipe: RecipeUser, context: Context) {
    compositeDisposable.add(myService.saveToMyList(recipe.id, LoginUser.loginEmail)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe { result ->

        }
    )
}

fun delete(recipe: RecipeUser, context: Context) {
    compositeDisposable.add(myService.deleteRecipe(recipe.id)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe { result ->
            context.startActivity(Intent(context, HomeActivity::class.java))
        }
    )
}

