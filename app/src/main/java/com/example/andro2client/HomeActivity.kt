package com.example.andro2client

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.annotation.ExperimentalCoilApi
import coil.compose.ImagePainter
import coil.compose.rememberImagePainter
import com.example.andro2client.ui.theme.Andro2ClientTheme
import com.example.andro2client.ui.theme.MainScreen
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import com.fasterxml.jackson.module.kotlin.readValue
import org.json.JSONArray
import org.json.JSONObject
import com.example.andro2client.model.Recipe
import com.example.andro2client.model.User
import java.io.File


class HomeActivity: ComponentActivity() {

    @ExperimentalFoundationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Andro2ClientTheme {
                MainScreen()

            }
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun HomeScreenView(modifier: Modifier = Modifier) {

    val listdatastate= RecipeList()
    bla(listdatastate)
}

@Composable
private fun RecipeList(): ArrayList<Recipe>? {
    val mylistdata = ArrayList<Recipe>()
    val mylistdatastate = remember {
        mutableStateOf<ArrayList<Recipe>?>(null)
    }

    compositeDisposable.add(myService.getRecipe()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe { result ->

            val answer = JSONArray(result)

            for (i in 0 until answer.length()) {
                val json_objectdetail: JSONObject =answer.getJSONObject(i)
                val model:Recipe= Recipe(
                    json_objectdetail.getString("_id"),
                    json_objectdetail.getString("time"),
                    json_objectdetail.getString("level"),
                    json_objectdetail.getString("type"),
                    json_objectdetail.getString("foodType"),
                    json_objectdetail.getString("creatorEmail"),
                );

                if(model.type=="public"){
                    mylistdata.add(model)
                }


            }
            mylistdatastate.value = mylistdata
        }
    )
    return mylistdatastate.value
}

@Composable
fun bla(mylistdata: ArrayList<Recipe>?){
    val context = LocalContext.current


if(mylistdata!=null){
    
    LazyColumn {

        items(mylistdata.size) {
            Card(
                modifier = Modifier
                    .padding(10.dp)
                    .fillMaxWidth()
                    .wrapContentHeight(),

                shape = RoundedCornerShape(20.dp),
                elevation = 8.dp,
                backgroundColor = MaterialTheme.colors.background
            )
            {


                Row(
                    Modifier.clickable {
                        val intent = Intent(context, RecipeActivity::class.java)
                        intent.putExtra("RECIPE_ID", mylistdata.get(it))
                        context.startActivity(intent)
                    }
                ) {

                    ImageLoader("R.drawable.cake")
                    Spacer(modifier = Modifier.width(1.dp))
                    Column(

                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth()
                            .align(Alignment.CenterVertically)) {
                        Text(text = mylistdata.get(it).time+ " | "+ mylistdata.get(it).level)
                        Spacer(modifier = Modifier.padding(5.dp))
                        Text(text = "By "+ mylistdata.get(it).creatorMail)
                    }
                }
            }
        }
    }
}
}

@ExperimentalCoilApi
@Composable
fun ImageLoader(image: String){

    Box(modifier = Modifier
        .height(150.dp)
        .width(150.dp),


        contentAlignment = Alignment.Center
    ){

        val painter1= rememberImagePainter(
            data=File("/andro2Client/app/src/main/res/uploads/cake.jpg"),
            builder = {
                error(R.drawable.error2)
            }
        )
        val printState = painter1.state
        Image(

            painter = painter1,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.size(120.dp)

        )

        if (printState is ImagePainter.State.Loading){
            CircularProgressIndicator()
        }
    }

}






