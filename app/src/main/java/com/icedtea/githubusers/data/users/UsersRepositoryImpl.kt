package com.icedtea.githubusers.data.users

import com.icedtea.githubusers.domain.users.User
import com.icedtea.githubusers.domain.users.UsersRepository
import com.icedtea.githubusers.utils.Either
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UsersRepositoryImpl @Inject constructor(
    private val service: UsersService
) : UsersRepository{
    override suspend fun getUsersList(
        since: Int,
        perPage: Int
    ): Either<String, List<User>> = try {
        val response = service.getUsersList(since, perPage)
        if (response.isSuccessful){
            response.body()?.let {
                return@let Either.Right(it)
            } ?: handleError()
        } else{
            handleError()
        }
    } catch (e: Exception) {
        handleError()
    }

    private fun handleError(): Either.Left<String> {
        return Either.Left(ERROR_MESSAGE)
    }
}

private const val ERROR_MESSAGE = "Failed to get Users"