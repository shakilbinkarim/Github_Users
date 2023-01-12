package com.icedtea.githubusers.di

import com.icedtea.githubusers.data.login.LoginRepositoryImpl
import com.icedtea.githubusers.data.users.UsersRepositoryImpl
import com.icedtea.githubusers.domain.login.LoginRepository
import com.icedtea.githubusers.domain.users.UsersRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindLoginRepository(
        loginRepository: LoginRepositoryImpl
    ): LoginRepository

    @Binds
    @Singleton
    abstract fun bindUsersRepository(
        usersRepository: UsersRepositoryImpl
    ): UsersRepository

}