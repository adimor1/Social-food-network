package com.example.myapplication

import android.content.Context
import android.content.Intent
import android.text.TextUtils
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
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
    val focusRequester = remember {
        FocusRequester()
    }

    val IngredientsState = remember { mutableStateOf(recipe.ingredients) }
    val InstructionsState = remember { mutableStateOf(recipe.instruction) }
    val levelState = remember { mutableStateOf(recipe.level) }
    val timeState = remember { mutableStateOf(recipe.time) }
    val typeState = remember { mutableStateOf(recipe.type )}
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

                        if(recipe.creatorMail != LoginUser.loginEmail && LoginUser.isAdmin!="true") {

                            Row() {
                                Icon(
                                    Icons.Rounded.TrendingUp,
                                    contentDescription = "",
                                    tint = Color.Black
                                )
                                Spacer(modifier = Modifier.width(5.dp))
                                Text(

                                    text = recipe.level
                                )
                            }

                            Row() {
                                Icon(
                                    Icons.Rounded.Update,
                                    contentDescription = "",
                                    tint = Color.Black
                                )
                                Spacer(modifier = Modifier.width(5.dp))
                                Text(
                                    text = recipe.time
                                )
                            }
                            Spacer(modifier = Modifier.size(15.dp))
                            Text(
                                text = "Ingredients:",
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp
                            )

                            Text(
                                text = recipe.ingredients
                            )
                            Spacer(modifier = Modifier.size(15.dp))
                            Text(
                                text = "Instruction:",
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp
                            )

                            Text(
                                text = recipe.instruction
                            )

                            Spacer(modifier = Modifier.size(15.dp))

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
                            }
                        }else{

                            Row() {

                                OutlinedTextField(
                                    modifier = Modifier
                                        .width(175.dp)
                                        .testTag("level"),
                                    value = levelState.value,
                                    onValueChange = {
                                        levelState.value = it
                                    },
                                    label = { Text("Level") },
                                    keyboardOptions = KeyboardOptions(
                                        keyboardType = KeyboardType.Email,
                                        imeAction = ImeAction.Next
                                    ),

                                    keyboardActions = KeyboardActions(
                                        onNext = {
                                            focusRequester.requestFocus()
                                        }
                                    )
                                )

                                Spacer(modifier = Modifier.size(10.dp))

                                OutlinedTextField(
                                    modifier = Modifier
                                        .width(175.dp)
                                        .testTag("time"),
                                    value = timeState.value,
                                    onValueChange = {
                                        timeState.value = it
                                    },
                                    label = { Text("Time") },
                                    keyboardOptions = KeyboardOptions(
                                        keyboardType = KeyboardType.Text,
                                        imeAction = ImeAction.Next
                                    ),

                                    keyboardActions = KeyboardActions(
                                        onNext = {
                                            focusRequester.requestFocus()
                                        }
                                    )
                                )
                            }

                            OutlinedTextField(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(90.dp)
                                    .testTag("ingredients"),
                                value = IngredientsState.value,
                                onValueChange = {
                                    IngredientsState.value = it
                                },
                                label = { Text("Ingredients") },
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Email,
                                    imeAction = ImeAction.Next
                                ),
                                keyboardActions = KeyboardActions(
                                    onNext = {
                                        focusRequester.requestFocus()
                                    }
                                )
                            )

                            OutlinedTextField(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(90.dp)
                                    .testTag("instructions"),
                                value = InstructionsState.value,
                                onValueChange = {
                                    InstructionsState.value = it
                                },
                                label = { Text("Instructions") },
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Email,
                                    imeAction = ImeAction.Next
                                ),
                                keyboardActions = KeyboardActions(
                                    onNext = {
                                        focusRequester.requestFocus()
                                    }
                                )
                            )


                            Column() {


                                Row() {
                                    RadioButton(
                                        selected = typeState.value == "private",
                                        onClick = { typeState.value = "private" },
                                        colors = RadioButtonDefaults.colors(Color.DarkGray)
                                    )
                                    Spacer(modifier = Modifier.size(16.dp))
                                    Text(text = "Private")
                                }
                                Spacer(modifier = Modifier.size(16.dp))
                                Row() {


                                    RadioButton(
                                        selected = typeState.value == "public",
                                        onClick = { typeState.value = "public" },
                                        colors = RadioButtonDefaults.colors(Color.DarkGray)
                                    )
                                    Spacer(modifier = Modifier.size(16.dp))
                                    Text(text = "Public")
                                }
                            }
                        }
                            Text(
                                text = "By " + recipe.creatorMail
                            )
                        Text(
                            text = spon
                        )
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {

                            if (recipe.creatorMail == LoginUser.loginEmail || LoginUser.isAdmin=="true") {

                                Row() {

                                    Button(
                                        modifier = Modifier.padding(top = 16.dp),
                                        onClick = {
                                            edit(
                                                recipe.id,
                                                levelState.value,
                                                timeState.value,
                                                IngredientsState.value,
                                                InstructionsState.value,
                                                typeState.value ,
                                                context)
                                        }) {
                                        Icon(
                                            Icons.Rounded.Save,
                                            contentDescription = "Localized description",
                                            tint = Color.White,
                                        )
                                        Text("Update")
                                    }
                                    Spacer(modifier = Modifier.size(16.dp))
                                    Button(
                                        modifier = Modifier.padding(top = 16.dp),
                                        onClick = {
                                            delete(recipe, context)
                                        }) {
                                        Icon(
                                            Icons.Rounded.Delete,
                                            contentDescription = "Localized description",
                                            tint = Color.White,
                                        )
                                        Text("Delete")
                                    }


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
}

fun edit(id:String, level: String, time: String, ingredients: String, instructions: String, type: String, context: Context) {
    compositeDisposable.add(myService.editRecipe(id, level, time, ingredients, instructions, type)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe { result ->
            context.startActivity(Intent(context, HomeActivity::class.java))
        }
    )
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

