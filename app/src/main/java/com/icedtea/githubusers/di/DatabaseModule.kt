package com.icedtea.githubusers.di

import android.content.Context
import androidx.room.Room
import com.icedtea.githubusers.data.GithubUsersDatabase
import com.icedtea.githubusers.data.users.local.UserRemoteKeysDao
import com.icedtea.githubusers.data.users.local.UsersDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class SingletonModule {

    @Singleton
    @Provides
    fun provideMovieDatabase(@ApplicationContext context: Context): GithubUsersDatabase =
        Room
            .databaseBuilder(
                context,
                GithubUsersDatabase::class.java,
                "github_users_database"
            ).build()

    @Singleton
    @Provides
    fun provideMoviesDao(githubUsersDatabase: GithubUsersDatabase): UsersDao =
        githubUsersDatabase.getUsersDao()

    @Singleton
    @Provides
    fun provideRemoteKeysDao(githubUsersDatabase: GithubUsersDatabase): UserRemoteKeysDao =
        githubUsersDatabase.getUsersRemoteKeysDao()
}