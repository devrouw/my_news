package com.lollipop.mynews.di.module

import com.lollipop.mynews.repository.source.ISourceRepository
import com.lollipop.mynews.repository.source.SourceRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module(includes = [NetworkModule::class])
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    abstract fun provideMainRepository(mainRepo: SourceRepository): ISourceRepository
}