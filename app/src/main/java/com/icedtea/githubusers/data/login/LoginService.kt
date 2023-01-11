package com.icedtea.githubusers.data.login

import com.squareup.moshi.Json
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Headers
import retrofit2.http.POST

interface LoginService {

    @Headers("Accept: application/json")
    @POST("login/oauth/access_token")
    @FormUrlEncoded
    suspend fun login(
        @Field("client_id") clientId: String,
        @Field("client_secret") clientSecret: String,
        @Field("code") code: String
    ): Response<AccessToken>
}

data class AccessToken(
    @field:Json(name = "access_token") val accessToken: String,
    val scope: String,
    @field:Json(name = "token_type") val tokenType: String
)