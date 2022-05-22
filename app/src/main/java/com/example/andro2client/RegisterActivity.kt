package com.example.andro2client

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
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

        Button(
            modifier = Modifier.padding(top = 16.dp),
            //enabled = usernameSate.value.isNotEmpty() && passwordSate.value.isNotEmpty(),
            onClick = {
                register(emailSate.value, nameSate.value, passwordSate.value, context)
            }) {
            Text("Create Account")
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
            }
        }
    )
}