package com.icedtea.githubusers.data.login

import com.icedtea.githubusers.domain.PreferenceStorage
import com.icedtea.githubusers.domain.login.LoginRepository
import com.icedtea.githubusers.utils.ResourceState
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
    ): ResourceState<String> = try {
        val response = service.login(
            clientId = clientId,
            clientSecret = clientSecret,
            code = code
        )
        if (response.isSuccessful){
            response.body()?.let {
                val accessToken = it.accessToken
                preferenceStorage.saveAccessToken(accessToken)
                return@let ResourceState.Success(accessToken)
            } ?: handleError()
        } else{
            handleError()
        }
    } catch (e: Exception) {
        handleError()
    }

    private suspend fun handleError(): ResourceState.Error<String> {
        preferenceStorage.saveAccessToken("")
        return ResourceState.Error(ERROR_MESSAGE)
    }

}

private const val ERROR_MESSAGE = "Failed to login user."