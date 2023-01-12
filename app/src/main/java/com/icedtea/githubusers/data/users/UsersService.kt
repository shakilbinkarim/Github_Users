package com.icedtea.githubusers.data.users

import com.icedtea.githubusers.domain.users.User
import com.icedtea.githubusers.domain.users.UserDetail
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface UsersService {
    @GET("users")
    suspend fun getUsersList(
        @Query("since") since: Int,
        @Query("per_page") perPage: Int,
    ): Response<List<User>>

    @GET("users/{username}")
    suspend fun getUserDetails(
        @Path("username") userName: String,
    ): Response<UserDetail>
}
