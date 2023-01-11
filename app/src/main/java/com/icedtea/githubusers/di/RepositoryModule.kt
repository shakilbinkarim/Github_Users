package com.icedtea.githubusers.di

import com.icedtea.githubusers.data.login.LoginRepositoryImpl
import com.icedtea.githubusers.domain.login.LoginRepository
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

}