package com.example.andro2client.Retrofit

import android.telecom.Call
import io.reactivex.Observable
import retrofit2.http.*
import retrofit2.http.HTTP




interface MyService {
    @POST ("register")
    @FormUrlEncoded
    fun registerUser(@Field("email") email:String,
                     @Field("name") name:String,
                     @Field("password") password:String): Observable<String>

    @POST ("login")
    @FormUrlEncoded
    fun loginUser(@Field("email") email:String,
                  @Field("password") password:String): Observable<String>

    @POST ("addRecipe")
    @FormUrlEncoded
    fun addRecipe(@Field("level") level:String,
                  @Field("time") time:String,
                  @Field("creatorEmail") creatorEmail:String): Observable<String>


    @GET ("getRecipe")
    fun getRecipe():Observable<String>

    @POST ("getUserDetails")
    @FormUrlEncoded
    fun getUserDetails(@Field("email") email:String):Observable<String>

    @POST ("getMyRecipe")
    @FormUrlEncoded
    fun getMyRecipe(@Field("creatorEmail") creatorEmail:String):Observable<String>

    @FormUrlEncoded
    @HTTP(method = "DELETE", path = "deleteRecipe", hasBody = true)
    fun deleteRecipe(@Field("id") id: String):Observable<String>
}