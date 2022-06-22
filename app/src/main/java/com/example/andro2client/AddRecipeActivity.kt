package com.example.andro2client

import kotlin.random.Random
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
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
import androidx.activity.result.launch
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
import androidx.compose.material.icons.rounded.AddAPhoto
import androidx.compose.material.icons.rounded.FileUpload
import androidx.compose.material.icons.rounded.Update
import androidx.compose.runtime.*
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import com.example.andro2client.model.LoginUser
import com.google.firebase.storage.FirebaseStorage
import java.io.ByteArrayOutputStream


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

    val nameState = remember { mutableStateOf("") }
    val IngredientsState = remember { mutableStateOf("") }
    val InstructionsState = remember { mutableStateOf("") }
    val levelState = remember { mutableStateOf("") }
    val timeState = remember { mutableStateOf("") }
    val typeState = remember { mutableStateOf("") }

    val context = LocalContext.current
    val myImage: Bitmap = BitmapFactory.decodeResource(
        Resources.getSystem(),
        android.R.mipmap.sym_def_app_icon
    )
    val result = remember { mutableStateOf<Bitmap>(myImage) }
    var imageUrl by remember { mutableStateOf<Uri?>(null) }
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        imageUrl = uri
    }
    val loadImage = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) {
        if (it != null) {
            result.value = it
            imageUrl = getImageUriFromBitmap(context, it)
        }
    }


    Column(
        modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        imageUrl?.let {
            if (Build.VERSION.SDK_INT < 28) {
                result.value =
                    MediaStore.Images.Media.getBitmap(context.contentResolver, it)
            } else {
                val source = ImageDecoder.createSource(context.contentResolver, it)
                result.value = ImageDecoder.decodeBitmap(source)
            }

            result.value?.let { bitmap ->
                Image(
                    bitmap = bitmap.asImageBitmap(),
                    contentDescription = "Gallery Image",
                    modifier = Modifier.size(110.dp)
                )
            }
        }

        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .testTag("name"),
            value = nameState.value,
            onValueChange = {
                nameState.value = it
            },
            label = { Text("Name") },
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


        //***type selector***//
        var mExpanded by remember { mutableStateOf(false) }
        val mCities =
            listOf("Italian", "Asian", "Japanese", "Mediterranean", "Dessert", "Mexican", "Indian")
        var mSelectedText by remember { mutableStateOf("") }
        var mTextFieldSize by remember { mutableStateOf(Size.Zero) }
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
                label = { Text("Type") },
                trailingIcon = {
                    Icon(icon, "contentDescription",
                        Modifier.clickable { mExpanded = !mExpanded })
                }
            )

            DropdownMenu(
                expanded = mExpanded,
                onDismissRequest = { mExpanded = false },
                modifier = Modifier
                    .width(with(LocalDensity.current) { mTextFieldSize.width.toDp() })
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

            //***type selector***//


        Spacer(modifier = Modifier.size(1.dp))

        Row {

            Column() {


                Row() {
                    RadioButton(
                        selected = typeState.value == "private",
                        onClick = { typeState.value = "private" },
                        colors = RadioButtonDefaults.colors(Color.DarkGray)
                    )
                    Spacer(modifier = Modifier.size(16.dp))
                    Text(text = "private")
                }
                Spacer(modifier = Modifier.size(16.dp))
                Row() {


                    RadioButton(
                        selected = typeState.value == "public",
                        onClick = { typeState.value = "public" },
                        colors = RadioButtonDefaults.colors(Color.DarkGray)
                    )
                    Spacer(modifier = Modifier.size(16.dp))
                    Text(text = "public")
                }

            }
            Spacer(modifier = Modifier.width(110.dp))
            Row() {
                val isChecked = remember { mutableStateOf(false) }
                Checkbox(checked = isChecked.value,
                    onCheckedChange = { isChecked.value = it },
                    colors = CheckboxDefaults.colors(Color.DarkGray))
                Spacer(modifier = Modifier.size(5.dp))
                Text(text = "Sponsored recipe")
            }

        }
        Spacer(modifier = Modifier.width(10.dp))

        Row() {
            Button(
                onClick = {
                    launcher.launch("image/*")

                },
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color.DarkGray,
                    contentColor = Color.DarkGray)
            ) {
                Icon(Icons.Rounded.FileUpload, contentDescription = "",   tint = Color.White)
            }
            Spacer(modifier = Modifier.size(1.dp))
            Button(
                onClick = {
                    loadImage.launch()

                }, colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color.DarkGray,
                contentColor = Color.DarkGray)
            ) {
                Icon(Icons.Rounded.AddAPhoto, contentDescription = "",   tint = Color.White)
            }
            Spacer(modifier = Modifier.width(150.dp))
            Button(
                onClick = {
                    addRecipe(
                        levelState.value,
                        timeState.value,
                        typeState.value,
                        mSelectedText,
                        context,
                        imageUrl
                    )
                }) {
                Text("Save")
            }
        }
    }
    }

fun getImageUriFromBitmap(context: Context, bitmap: Bitmap): Uri{
    val bytes = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
    val path = MediaStore.Images.Media.insertImage(context.contentResolver, bitmap, "Title", null)
    return Uri.parse(path.toString())
}

fun addRecipe(level: String, time:String, type:String, foodtype: String, context: Context, imageUrl: Uri?) {


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

    val imageRec = Random.nextInt(0, 100000)

    compositeDisposable.add(myService.addRecipe(level, time, type, foodtype, LoginUser.loginEmail, imageRec.toString())
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe { result ->
            Toast.makeText(context, ""+result, Toast.LENGTH_SHORT).show()
            addImage(imageRec.toString(), imageUrl)
        }
    )
}

fun addImage(imageRec: String, imageUrl: Uri?) {

    val storageReference = FirebaseStorage.getInstance().getReference(imageRec)
    if (imageUrl != null) {
        storageReference.putFile(imageUrl).addOnSuccessListener {

        }
    }

}



