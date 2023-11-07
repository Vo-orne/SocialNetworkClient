package com.example.myprofile.domain

import com.example.myprofile.data.model.UserResponse
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface ApiService {

    @POST("users")
    @FormUrlEncoded
    fun registerUser(
        @Field("email") email: String,
        @Field("password") password: String,
        @Field("name") name: String?,
        @Field("phone") phone: String?,
        @Field("address") address: String?,
        @Field("career") career: String?,
        @Field("birthday") birthday: String?,
        @Field("facebook") facebook: String?,
        @Field("instagram") instagram: String?,
        @Field("twitter") twitter: String?,
        @Field("linkedin") linkedin: String?
    ): Call<UserResponse>
}
