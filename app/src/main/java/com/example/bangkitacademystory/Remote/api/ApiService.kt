package com.example.bangkitacademystory.Remote.api

import com.example.bangkitacademystory.Remote.response.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface ApiService {
    @FormUrlEncoded
    @POST("register")
    suspend fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): RegisterResponse

    @FormUrlEncoded
    @POST("login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): LoginResponse

    @GET("stories")
    suspend fun getStories(): AllStoryResponse

    @Multipart
    @POST("stories")
    suspend fun postNewStory(
        @Part photo: MultipartBody.Part,
        @Part("description") description: RequestBody
    ): NewStoryResponse

    @GET("stories/{id}")
    suspend fun getStoryDetail(
        @Path("id") id: String
    ): DetailStoryResponse

}