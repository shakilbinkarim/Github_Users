package com.icedtea.githubusers.data

import androidx.room.Database
import com.icedtea.githubusers.data.users.local.UserRemoteKeysDao
import com.icedtea.githubusers.data.users.local.UsersDao
import com.icedtea.githubusers.data.users.local.UsersRemoteKeys
import com.icedtea.githubusers.domain.users.User

@Database(
    entities = [
        User::class,
        UsersRemoteKeys::class
    ],
    version = 1
)
abstract class GithubUsersDatabase {
    abstract fun getUsersDao(): UsersDao
    abstract fun getUsersRemoteKeysDao(): UserRemoteKeysDao
}