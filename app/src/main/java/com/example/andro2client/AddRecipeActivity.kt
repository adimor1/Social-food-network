package com.example.andro2client

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.example.andro2client.model.LoginUser
import com.example.andro2client.ui.theme.Andro2ClientTheme
import com.example.andro2client.ui.theme.MainScreen
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.jar.Manifest
import androidx.compose.material.Text
import androidx.activity.result.contract.ActivityResultContracts.GetContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.andro2client.ui.theme.Purple500
import dev.chrisbanes.accompanist.glide.GlideImage

class AddRecipeActivity : ComponentActivity() {

    private var imageUriState = mutableStateOf<Uri?>(null)

    @ExperimentalFoundationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Andro2ClientTheme {
                Surface(color = MaterialTheme.colors.background) {
                    AddRecipeView()
                    MainScreen()

                }
            }
        }
    }

}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun AddRecipeView(modifier: Modifier = Modifier) {
    val focusRequester = remember {
        FocusRequester()
    }
    val levelSate = remember { mutableStateOf("") }
    val timeSate = remember { mutableStateOf("") }

    val context = LocalContext.current

    Column(
        modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .testTag("level"),
            value = levelSate.value,
            onValueChange = {
                levelSate.value = it
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
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .testTag("time"),
            value = timeSate.value,
            onValueChange = {
                timeSate.value = it
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

        Button(
            modifier = Modifier.padding(top = 16.dp),
            //enabled = usernameSate.value.isNotEmpty() && passwordSate.value.isNotEmpty(),
            onClick = {
                addRecipe(levelSate.value, timeSate.value, context)
            }) {
            Text("Add recipe")
        }
        ImagePicker()


    }
}

fun addRecipe(level: String, time:String, context: Context) {

    if(TextUtils.isEmpty(level))
    {
        Toast.makeText(context, "level can not be null or empty", Toast.LENGTH_SHORT).show()
        return;
    }

    if(TextUtils.isEmpty(time))
    {
        Toast.makeText(context, "time can not be null or empty", Toast.LENGTH_SHORT).show()
        return;
    }

    compositeDisposable.add(myService.addRecipe(level, time, LoginUser.loginEmail)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe { result ->
            Toast.makeText(context, ""+result, Toast.LENGTH_SHORT).show()
        }
    )

    context.startActivity(Intent(context, AddRecipeActivity::class.java))
}


@Composable
fun ImagePicker() {
    var imageUrl by remember { mutableStateOf<Uri?>(null) }
    val context = LocalContext.current
    val bitmap = remember { mutableStateOf<Bitmap?>(null) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()) { uri: Uri? ->
        imageUrl = uri
    }

    Column {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(androidx.compose.ui.graphics.Color.White)
                .padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            imageUrl?.let {
                if (Build.VERSION.SDK_INT < 28) {
                    bitmap.value = MediaStore.Images.Media.getBitmap(context.contentResolver, it)
                } else {
                    val source = ImageDecoder.createSource(context.contentResolver, it)
                    bitmap.value = ImageDecoder.decodeBitmap(source)
                }

                bitmap.value?.let { bitmap ->
                    Image(
                        bitmap = bitmap.asImageBitmap(),
                        contentDescription = "Gallery Image",
                        modifier = Modifier.size(200.dp)
                    )
                }
            }

            Button(
                onClick = {
                    launcher.launch("image/*")
                }
            ) {
                Text(
                    "Click Image",
                )
            }

            Spacer(modifier = Modifier.padding(20.dp))
        }
    }
}

