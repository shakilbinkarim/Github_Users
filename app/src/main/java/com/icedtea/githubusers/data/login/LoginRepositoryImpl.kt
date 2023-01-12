package com.icedtea.githubusers.data.login

import com.icedtea.githubusers.domain.PreferenceStorage
import com.icedtea.githubusers.domain.login.LoginRepository
import com.icedtea.githubusers.utils.Either
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LoginRepositoryImpl @Inject constructor(
    private val preferenceStorage: PreferenceStorage,
    private val service: LoginService
) : LoginRepository {

    override suspend fun login(
        clientId: String,
        clientSecret: String,
        code: String
    ): Either<String, String> = try {
        val response = service.login(
            clientId = clientId,
            clientSecret = clientSecret,
            code = code
        )
        if (response.isSuccessful){
            response.body()?.let {
                val accessToken = it.accessToken
                preferenceStorage.saveAccessToken(accessToken)
                return@let Either.Right(accessToken)
            } ?: handleError()
        } else{
            handleError()
        }
    } catch (e: Exception) {
        handleError()
    }

    private suspend fun handleError(): Either.Left<String> {
        preferenceStorage.saveAccessToken("")
        return Either.Left(ERROR_MESSAGE)
    }

}

private const val ERROR_MESSAGE = "Failed to login user."