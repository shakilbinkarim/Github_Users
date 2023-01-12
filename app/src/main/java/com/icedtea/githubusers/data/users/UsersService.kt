package com.icedtea.githubusers.data.users

import com.icedtea.githubusers.domain.users.User
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.GET

interface UsersService {
    @GET("users")
    suspend fun getUsersList(
        @Field("since") since: Int,
        @Field("per_page") perPage: Int,
    ): Response<List<User>>
}
