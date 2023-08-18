package com.lollipop.mynews.di.module

import com.lollipop.mynews.service.network.ApiNetwork
import com.lollipop.mynews.di.abstraction.Api
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Singleton
    @Provides
    fun provideApiService(@Api retrofit: Retrofit): ApiNetwork =
        retrofit.create(ApiNetwork::class.java)
}