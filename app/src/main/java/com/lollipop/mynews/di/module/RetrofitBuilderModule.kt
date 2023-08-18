package com.lollipop.mynews.di.module

import com.lollipop.mynews.BuildConfig
import com.lollipop.mynews.di.abstraction.Api
import com.lollipop.mynews.helper.Constant
import com.lollipop.mynews.service.network.HeaderInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RetrofitBuilderModule {
    @Singleton
    @Provides
    fun provideMoshi(): MoshiConverterFactory = MoshiConverterFactory.create()

    @Provides
    @Singleton
    fun provideApiKey(): String {
        return "758612bb14e842b6b77dc39d4eb5c50d"
    }

    @Provides
    @Singleton
    fun provideHeaderInterceptor(apiKey: String): HeaderInterceptor {
        return HeaderInterceptor(apiKey)
    }

    @Singleton
    @Provides
    fun provideOkHttpClient(
        headerInterceptor: HeaderInterceptor
    ): OkHttpClient =
        OkHttpClient.Builder()
            .addNetworkInterceptor(headerInterceptor)
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()

    @Singleton
    @Provides
    @Api
    fun provideRetrofitApi(
        moshiConverterFactory: MoshiConverterFactory,
        okHttpClient: OkHttpClient
    ): Retrofit = Retrofit.Builder()
        .addConverterFactory(moshiConverterFactory)
        .client(okHttpClient)
        .baseUrl(Constant.URL.BASE)
        .build()
}