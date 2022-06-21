package com.example.andro2client

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import com.example.andro2client.model.LoginUser
import com.example.andro2client.ui.theme.Andro2ClientTheme
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class RegisterActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Andro2ClientTheme {
                Surface(color = MaterialTheme.colors.background) {
                    RegisterView()
                }
            }
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun RegisterView(modifier: Modifier = Modifier) {
    val focusRequester = remember {
        FocusRequester()
    }
    val emailSate = remember { mutableStateOf("") }
    val nameSate = remember { mutableStateOf("") }
    val passwordSate = remember { mutableStateOf("") }
    val isVisibility = remember {
        mutableStateOf(false)
    }

    val keyboardController = LocalSoftwareKeyboardController.current
    val context = LocalContext.current

    Column(
        modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Create Account", fontSize = 25.sp)
        Spacer(modifier = Modifier.size(5.dp))

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth().testTag("email"),
            value = emailSate.value,
            onValueChange = {
                emailSate.value = it
            },
            label = { Text("Email") },
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
            modifier = Modifier.fillMaxWidth().focusRequester(focusRequester).testTag("password"),
            value = passwordSate.value,
            onValueChange = { passwordSate.value = it },
            label = { Text("Password") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            visualTransformation = if (isVisibility.value) {
                VisualTransformation.None
            } else {
                PasswordVisualTransformation()
            },
            trailingIcon = {
                IconButton(onClick = { isVisibility.value = !isVisibility.value }) {
                    Icon(
                        imageVector = if (isVisibility.value) {
                            Icons.Filled.Visibility
                        } else {
                            Icons.Filled.VisibilityOff
                        }, contentDescription = null
                    )
                }
            },

            keyboardActions = KeyboardActions(
                onDone = {
                    keyboardController?.hide()
                }
            )
        )
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth().testTag("birthYear"),
            value = nameSate.value,
            onValueChange = {
                nameSate.value = it
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
        var mSelectedText by remember { mutableStateOf("") }
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
        var mSelectedTextType by remember { mutableStateOf("") }
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

        val typeState = remember { mutableStateOf("") }
        Row {
            RadioButton(
                selected = typeState.value=="Female",
                onClick = { typeState.value="Female" },
                colors = RadioButtonDefaults.colors(Color.Gray)
            )
            Spacer(modifier = Modifier.size(16.dp))
            Text(text = "Female")
            Spacer(modifier = Modifier.size(16.dp))
            RadioButton(
                selected = typeState.value=="Male",
                onClick = { typeState.value="Male" },
                colors = RadioButtonDefaults.colors(Color.Gray)
            )
            Spacer(modifier = Modifier.size(16.dp))
            Text(text = "Male")
        }

        Spacer(modifier = Modifier.size(5.dp))

        Button(
            modifier = Modifier.padding(top = 16.dp),
            //enabled = usernameSate.value.isNotEmpty() && passwordSate.value.isNotEmpty(),
            onClick = {
                register(emailSate.value, nameSate.value, passwordSate.value, context)
            }) {
            Text("Create")
        }
    }
}

fun register(email: String, name:String, password: String, context: Context) {

    if(TextUtils.isEmpty(email))
    {
        Toast.makeText(context, "email can not be null or empty", Toast.LENGTH_SHORT).show()
        return;
    }

    if(TextUtils.isEmpty(name))
    {
        Toast.makeText(context, "name can not be null or empty", Toast.LENGTH_SHORT).show()
        return;
    }

    if(TextUtils.isEmpty(password))
    {
        Toast.makeText(context, "password can not be null or empty", Toast.LENGTH_SHORT).show()
        return;
    }

    compositeDisposable.add(myService.registerUser(email, name, password)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe { result ->
            Toast.makeText(context, ""+result, Toast.LENGTH_SHORT).show()

            if(result=="\"Registartion success\""){
                context.startActivity(Intent(context, HomeActivity::class.java))
                LoginUser.loginEmail = email

            }
        }
    )
}