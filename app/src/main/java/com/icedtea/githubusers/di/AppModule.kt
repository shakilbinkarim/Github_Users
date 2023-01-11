package com.icedtea.githubusers.di

import com.icedtea.githubusers.BuildConfig
import com.icedtea.githubusers.data.login.LoginService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    /***
     * Provides an application level [CoroutineScope]. This scope is automatically cancelled
     * when the application is destroyed. Therefore we do not need to manually cancel it.
     */
    @Singleton
    @Provides
    fun provideApplicationCoroutineScope(): CoroutineScope = CoroutineScope(SupervisorJob())

    @Provides
    @Singleton
    fun provideLoginService(): LoginService {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .client(
                OkHttpClient.Builder()
                .addInterceptor(
                    HttpLoggingInterceptor().apply {
                        level = HttpLoggingInterceptor.Level.BASIC
                    }
                ).build()
            )
            .build()
            .create()
    }

}