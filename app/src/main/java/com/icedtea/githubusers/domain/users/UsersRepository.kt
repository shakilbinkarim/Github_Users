package com.icedtea.githubusers.domain.users

import com.icedtea.githubusers.utils.DEFAULT_PAGE_SIZE

interface UsersRepository {

    suspend fun getUsersList(
        since: Int = 0,
        perPage: Int = DEFAULT_PAGE_SIZE,
    ): List<User>

}