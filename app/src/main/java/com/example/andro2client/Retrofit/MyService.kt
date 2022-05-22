package com.example.andro2client.Retrofit

import android.telecom.Call
import io.reactivex.Observable
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

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
                  @Field("time") time:String): Observable<String>


    @GET ("getRecipe")
    fun getRecipe():Observable<String>

    @GET ("getUserDetails")
    fun getUserDetails(@Field("email") email:String):Observable<String>
}