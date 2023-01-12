package com.icedtea.githubusers.data.users

import com.icedtea.githubusers.domain.users.User
import com.icedtea.githubusers.domain.users.UserDetail
import com.icedtea.githubusers.domain.users.UsersRepository
import com.icedtea.githubusers.utils.Either
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UsersRepositoryImpl @Inject constructor(
    private val service: UsersService
) : UsersRepository {
    @Throws(IllegalStateException::class)
    override suspend fun getUsersList(
        since: Int,
        perPage: Int
    ): List<User> {
        var list: List<User> = emptyList()
        val response = service.getUsersList(since, perPage)
        if (response.isSuccessful) {
            response.body()?.let {
                list = it
            }
        } else {
            throw IllegalStateException("Couldn't get users list")
        }
        return list
    }

    override suspend fun getUserDetail(userName: String): Either<String, UserDetail> {
        var result: Either<String, UserDetail> = Either.Left(ERROR_MESSAGE)
        return try {
            val response = service.getUserDetails(
                userName
            )
            response.body()?.let {
                result = Either.Right(it)
            }
            result
        } catch (e: Exception) {
            result
        }
    }
}

private const val ERROR_MESSAGE = "Failed to login user details."
