package com.icedtea.githubusers.di

import com.icedtea.githubusers.BuildConfig
import com.icedtea.githubusers.data.login.LoginService
import com.icedtea.githubusers.data.users.UsersService
import com.icedtea.githubusers.domain.PreferenceStorage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create
import javax.inject.Inject
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
    fun provideLoginService(): LoginService = Retrofit.Builder()
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

    @Provides
    @Singleton
    fun provideUserService(
        preferenceStorage: PreferenceStorage
    ): UsersService {
        var token = ""
        runBlocking {
            token = preferenceStorage.accessToken.first()
        }
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_USER_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .client(getRetrofitClient(token))
            .build()
            .create()

    }

    private fun getRetrofitClient(token: String): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor { chain ->
                chain.proceed(chain.request().newBuilder().also {
                    it.addHeader(
                        "Authorization",
                        "Bearer $token"
                    )
                    it.addHeader("Accept", "application/json")
                }.build())
            }.also { client ->
                if (BuildConfig.DEBUG) {
                    val logging = HttpLoggingInterceptor()
                    logging.setLevel(HttpLoggingInterceptor.Level.BODY)
                    client.addInterceptor(logging)
                }
            }.build()
    }

}