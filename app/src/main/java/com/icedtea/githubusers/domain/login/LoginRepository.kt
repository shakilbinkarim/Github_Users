package com.icedtea.githubusers.domain.login

import com.icedtea.githubusers.utils.ResourceState
import kotlinx.coroutines.flow.Flow

interface LoginRepository {

    suspend fun login(
        clientId: String,
        clientSecret: String,
        code: String
    ): ResourceState<String>

}