package com.example.andro2client.Retrofit

import io.reactivex.Observable
import retrofit2.http.*
import retrofit2.http.HTTP

interface MyService {
    @POST ("register")
    @FormUrlEncoded
    fun registerUser(@Field("email") email:String,
                     @Field("name") name:String,
                     @Field("password") password:String,
                     @Field("birth") birth:String,
                     @Field("favorite") favorite:String,
                     @Field("type") type:String,
                     @Field("gender") gender:String): Observable<String>

    @POST ("login")
    @FormUrlEncoded
    fun loginUser(@Field("email") email:String,
                  @Field("password") password:String): Observable<String>

    @POST ("addRecipe")
    @FormUrlEncoded
    fun addRecipe(@Field("level") level:String,
                  @Field("time") time:String,
                  @Field("type") type:String,
                  @Field("foodType") foodType:String,
                  @Field("creatorEmail") creatorEmail:String,
                  @Field("imageRec") imageRec:String,
                  @Field("name") name:String,
                  @Field("ingredients") ingredients:String,
                  @Field("instruction") instruction:String,
                  @Field("sponsored") sponsored:Boolean,
    ): Observable<String>


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

    @FormUrlEncoded
    @HTTP(method = "PUT", path = "editUser", hasBody = true)
    fun editUser(@Field("id") id: String,
                 @Field("name") name:String,
                 @Field("birth") birth:String,
                 @Field("favorite") favorite: String,
                 @Field("type") type:String,
                 @Field("gender") gender: String):Observable<String>

    @FormUrlEncoded
    @HTTP(method = "PUT", path = "savetomylist", hasBody = true)
    fun saveToMyList(@Field("id") id: String,
                 @Field("email") email:String):Observable<String>

    @POST ("getRecipeById")
    @FormUrlEncoded
    fun getRecipeById(@Field("email") email:String):Observable<String>

    @GET ("getrecipeuser")
    fun getrecipeuser():Observable<String>


    @FormUrlEncoded
    @HTTP(method = "PUT", path = "editRecipe", hasBody = true)
    fun editRecipe(@Field("id") id: String,
                 @Field("level") level:String,
                 @Field("time") time:String,
                 @Field("ingredients") ingredients: String,
                 @Field("instructions") instructions:String,
                 @Field("type") type: String):Observable<String>

    @GET ("getUsers")
    fun getUsers():Observable<String>
}