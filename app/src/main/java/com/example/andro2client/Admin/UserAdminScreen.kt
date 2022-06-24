package com.example.andro2client.Admin

import android.content.Context
import com.example.andro2client.model.User

import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import coil.compose.ImagePainter
import coil.compose.rememberImagePainter
import com.example.andro2client.*
import com.example.andro2client.R
import com.example.andro2client.model.RecipeUser
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

@Composable
fun UserScreen(user: User){
    val focusRequester = remember {
        FocusRequester()
    }

    if(user!=null){
        val context = LocalContext.current
        val username = user!!.name
        val nameSate = remember { mutableStateOf(username) }
        val birthSate = remember { mutableStateOf(user.birth) }
        val isBlueVState = remember { mutableStateOf(user.isBlueV) }


        Column(
            modifier= Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Row() {

                if(isBlueVState.value=="true") {
                    Box(
                        modifier = Modifier
                            .height(40.dp)
                            .width(40.dp),


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
                            modifier = Modifier.size(50.dp)

                        )

                        if (printState is ImagePainter.State.Loading) {
                            CircularProgressIndicator()
                        }
                    }
                    Spacer(modifier = Modifier.size(16.dp))
                }
                Text(
                    text = user.email,
                    fontSize = 25.sp
                )
            }

            Spacer(modifier = Modifier.size(30.dp))

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth().testTag("name"),
                value = nameSate.value,
                onValueChange = {
                    nameSate.value = it
                },
                label = { Text("Name") },
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

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth().testTag("birth"),
                value = birthSate.value,
                onValueChange = {
                    birthSate.value = it
                },
                label = { Text("Birth year") },
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

            val favorit = listOf("Italian", "Asian", "Japanese", "Mediterranean", "Dessert", "Mexican", "Indian")
            var mExpanded by remember { mutableStateOf(false) }
            var mSelectedText by remember { mutableStateOf(user.favorite) }
            var mTextFieldSize by remember { mutableStateOf(Size.Zero)}
            val icon = if (mExpanded)
                Icons.Filled.KeyboardArrowUp
            else
                Icons.Filled.KeyboardArrowDown


            OutlinedTextField(
                value = mSelectedText,
                onValueChange = { mSelectedText = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .onGloballyPositioned { coordinates ->
                        mTextFieldSize = coordinates.size.toSize()
                    },
                label = {Text("Favorite food")},
                trailingIcon = {
                    Icon(icon,"contentDescription",
                        Modifier.clickable { mExpanded = !mExpanded })
                }
            )

            DropdownMenu(
                expanded = mExpanded,
                onDismissRequest = { mExpanded = false },
                modifier = Modifier
                    .width(with(LocalDensity.current){mTextFieldSize.width.toDp()})
            ) {
                favorit.forEach { label ->
                    DropdownMenuItem(onClick = {
                        mSelectedText = label
                        mExpanded = false
                    }) {
                        Text(text = label)
                    }
                }
            }

            val type = listOf("Private", "Blogger", "Cooker", "Chef",  "Workshop editor", "Food critic")
            var mExpandedType by remember { mutableStateOf(false) }
            var mSelectedTextType by remember { mutableStateOf(user.type) }
            var mTextFieldSizeType by remember { mutableStateOf(Size.Zero)}
            val iconType = if (mExpandedType)
                Icons.Filled.KeyboardArrowUp
            else
                Icons.Filled.KeyboardArrowDown


            OutlinedTextField(
                value = mSelectedTextType,
                onValueChange = { mSelectedTextType = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .onGloballyPositioned { coordinates ->
                        mTextFieldSizeType = coordinates.size.toSize()
                    },
                label = {Text("User Type")},
                trailingIcon = {
                    Icon(iconType,"contentDescription",
                        Modifier.clickable { mExpandedType = !mExpandedType })
                }
            )

            DropdownMenu(
                expanded = mExpandedType,
                onDismissRequest = { mExpandedType = false },
                modifier = Modifier
                    .width(with(LocalDensity.current){mTextFieldSizeType.width.toDp()})
            ) {
                type.forEach { label ->
                    DropdownMenuItem(onClick = {
                        mSelectedTextType = label
                        mExpandedType = false
                    }) {
                        Text(text = label)
                    }
                }
            }
            Spacer(modifier = Modifier.size(5.dp))

            val genderState = remember { mutableStateOf(user.gender) }
            Row {
                RadioButton(
                    selected = genderState.value=="Female",
                    onClick = { genderState.value="Female" },
                    colors = RadioButtonDefaults.colors(Color.Gray)
                )
                Spacer(modifier = Modifier.size(16.dp))
                Text(text = "Female")
                Spacer(modifier = Modifier.size(16.dp))
                RadioButton(
                    selected = genderState.value=="Male",
                    onClick = { genderState.value="Male" },
                    colors = RadioButtonDefaults.colors(Color.Gray)
                )
                Spacer(modifier = Modifier.size(16.dp))
                Text(text = "Male")
            }


            Button(
                modifier = Modifier.padding(top = 16.dp),
                onClick = {
                    editUser(user, context, nameSate.value, birthSate.value, mSelectedText,mSelectedTextType ,genderState.value)
                }) {
                Text("Update")
            }

            Button(
                modifier = Modifier.padding(top = 16.dp),
                onClick = {
                    deleteUser(user, context)
                }) {
                Text("Delte")
            }

        }


    }
}

fun deleteUser(user: User, context: Context) {
    compositeDisposable.add(myService.deleteUser(user.id)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe { result ->
            context.startActivity(Intent(context, UserAdminActivity::class.java))
        }
    )
}
