package com.example.andro2client

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.andro2client.model.LoginUser
import com.example.andro2client.ui.theme.Andro2ClientTheme
import com.example.andro2client.ui.theme.MainScreen
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import androidx.compose.material.Text
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.*
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.toSize
import java.io.IOException

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

    val levelState = remember { mutableStateOf("") }
    val timeState = remember { mutableStateOf("") }
    val typeState = remember { mutableStateOf("") }

    //***image***
    var imageUrl by remember { mutableStateOf<Uri?>(null) }
    val bitmap = remember { mutableStateOf<Bitmap?>(null) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()) { uri: Uri? ->
        imageUrl = uri
    }
    //***image***

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
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
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
        Spacer(modifier = Modifier.size(16.dp))

        Row {
            RadioButton(
                selected = typeState.value=="private",
                onClick = { typeState.value="private" },
                colors = RadioButtonDefaults.colors(Color.Gray)
            )
            Spacer(modifier = Modifier.size(16.dp))
            Text(text = "private")
            Spacer(modifier = Modifier.size(16.dp))
            RadioButton(
                selected = typeState.value=="public",
                onClick = { typeState.value="public" },
                colors = RadioButtonDefaults.colors(Color.Gray)
            )
            Spacer(modifier = Modifier.size(16.dp))
            Text(text = "public")
        }

        //***type selector***//
        var mExpanded by remember { mutableStateOf(false) }
        val mCities = listOf("Italian", "Asian", "Japanese", "Mediterranean", "Dessert", "Mexican", "Indian")
        var mSelectedText by remember { mutableStateOf("") }
        var mTextFieldSize by remember { mutableStateOf(Size.Zero)}
        val icon = if (mExpanded)
            Icons.Filled.KeyboardArrowUp
        else
            Icons.Filled.KeyboardArrowDown

        Column(Modifier.padding(20.dp)) {

            OutlinedTextField(
                value = mSelectedText,
                onValueChange = { mSelectedText = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .onGloballyPositioned { coordinates ->
                        mTextFieldSize = coordinates.size.toSize()
                    },
                label = {Text("Type")},
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
                mCities.forEach { label ->
                    DropdownMenuItem(onClick = {
                        mSelectedText = label
                        mExpanded = false
                    }) {
                        Text(text = label)
                    }
                }
            }
        }
        //***type selector***//

        Button(
            modifier = Modifier.padding(top = 16.dp),
            onClick = {
                mSelectedText
                val imgaeByte = readBytes(context, imageUrl)

                addRecipe(levelState.value, timeState.value, typeState.value, mSelectedText, imgaeByte, context)

            }) {
            Text("Add recipe")
        }
        //ImagePicker()


        //***image***
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
        //***image***
    }
}

fun addRecipe(level: String, time:String, type:String, foodtype: String, byteArray: ByteArray?, context: Context) {

    if(!(foodtype=="Italian" ||foodtype=="Asian"||foodtype=="Japanese"||foodtype=="Mediterranean"|| foodtype=="Dessert"||foodtype=="Mexican"||foodtype=="Indian")){
        Toast.makeText(context, "choose type from the list", Toast.LENGTH_SHORT).show()
        return;
    }

    if(TextUtils.isEmpty(type))
    {
        Toast.makeText(context, "type can not be null or empty", Toast.LENGTH_SHORT).show()
        return;
    }

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

    compositeDisposable.add(myService.addRecipe(level, time, type, foodtype, LoginUser.loginEmail, byteArray)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe { result ->
            Toast.makeText(context, ""+result, Toast.LENGTH_SHORT).show()
        }
    )
}

@Throws(IOException::class)
private fun readBytes(context: Context, uri: Uri?): ByteArray? =
    context.contentResolver.openInputStream(uri!!)?.buffered()?.use { it.readBytes() }



