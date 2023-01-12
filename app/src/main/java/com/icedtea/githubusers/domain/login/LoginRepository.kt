package com.icedtea.githubusers.domain.login

import com.icedtea.githubusers.utils.Either

interface LoginRepository {

    suspend fun login(
        clientId: String,
        clientSecret: String,
        code: String
    ): Either<String, String>

}