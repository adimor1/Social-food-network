package com.example.andro2client

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.ImagePainter
import coil.compose.rememberImagePainter
import com.example.andro2client.Admin.AdminHomeActivity
import com.example.andro2client.LocalDB.DBManager
import com.example.andro2client.Retrofit.MyService
import com.example.andro2client.Retrofit.RetrofitClient
import com.example.andro2client.model.LoginUser
import com.example.andro2client.ui.theme.Andro2ClientTheme
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import org.json.JSONObject

lateinit var myService: MyService

internal var compositeDisposable= CompositeDisposable()

class MainActivity : ComponentActivity() {
    override fun onStop(){
        compositeDisposable.clear()
        super.onStop()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val dbManager: DBManager



        dbManager =  DBManager(this);
        dbManager.open();
        val userName:String = dbManager.userexit( )
        if(userName!=""){
            if( userName.contains("Admin"))
            {
                LoginUser.loginEmail = userName.split("-").get(0)
                LoginUser.isAdmin="true"
                this.startActivity(Intent(this, AdminHomeActivity::class.java))
            }
            else {
                LoginUser.loginEmail = userName
                LoginUser.isAdmin="false"
                this.startActivity(Intent(this, HomeActivity::class.java))
            }
        }
        dbManager.close()


        setContent {
            Andro2ClientTheme {
                Surface(color = MaterialTheme.colors.background) {
                    LoginView()
               }
            }
        }

        val retrofit = RetrofitClient.getInstance()
        myService = retrofit.create(MyService::class.java)

    }


}



@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun LoginView(modifier: Modifier = Modifier) {
    val focusRequester = remember {
        FocusRequester()
    }
    val usernameSate = remember { mutableStateOf("") }
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

        Box(modifier = Modifier
            .height(250.dp)
            .width(300.dp),


            contentAlignment = Alignment.Center
        ){

            val painter1= rememberImagePainter(
                data="https://firebasestorage.googleapis.com/v0/b/andro2client.appspot.com/o/app%2Flogo.jpg?alt=media&token=8f9e729a-2d62-4578-9785-253a0238ed9a",
                builder = {
                    error(R.drawable.error2)
                }
            )
            val printState = painter1.state
            Image(

                painter = painter1,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.size(300.dp)

            )

            if (printState is ImagePainter.State.Loading){
                CircularProgressIndicator()
            }
        }

        Text("InstaCook",  fontSize = 50.sp)

        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .testTag("user"),
            value = usernameSate.value,
            onValueChange = {
                usernameSate.value = it
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
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusRequester)
                .testTag("password"),
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
                loginUser(usernameSate.value, passwordSate.value, context)
            }) {
            Text("Login")
        }

        Button(
            modifier = Modifier.padding(top = 16.dp),
            //enabled = usernameSate.value.isNotEmpty() && passwordSate.value.isNotEmpty(),
            onClick = {
                context.startActivity(Intent(context, RegisterActivity::class.java))
            }, colors = ButtonDefaults.buttonColors(
                backgroundColor = Color.DarkGray,
                contentColor = Color.White)
        ) {
            Text("Don't have an account? Sign up")
        }

    }
}

fun loginUser(userName: String, password: String, context: Context) {

    if(TextUtils.isEmpty(userName))
    {
        Toast.makeText(context, "email can not be null or empty", Toast.LENGTH_SHORT).show()
        return;
    }

    if(TextUtils.isEmpty(password))
    {
        Toast.makeText(context, "password can not be null or empty", Toast.LENGTH_SHORT).show()
        return;
    }

    compositeDisposable.add(myService.loginUser(userName, password)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe { result ->


            if(result!="\"Email not exist\"" && result!="\"Worng password\""){

                val answer = JSONObject(result)
                val isAdmin = answer.getString("isAdmin")

                if(isAdmin == "true"){
                    context.startActivity(Intent(context, AdminHomeActivity::class.java))
                    LoginUser.isAdmin = "true"
                }
                else{
                    context.startActivity(Intent(context, HomeActivity::class.java))
                    LoginUser.isAdmin="false"
                }
                LoginUser.loginEmail = userName

                val dbManager: DBManager
                dbManager =  DBManager(context);
                dbManager.open();
                if(isAdmin=="true"){
                    dbManager.Sighningup(0, userName+"-Admin", password)
                }
                else{
                    dbManager.Sighningup(0, userName, password)
                }
                dbManager.close()

            }
            else{
                Toast.makeText(context, ""+result, Toast.LENGTH_SHORT).show()
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    Andro2ClientTheme {
        LoginView()
    }
}