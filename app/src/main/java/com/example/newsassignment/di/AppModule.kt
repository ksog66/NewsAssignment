package com.example.newsassignment.di

import com.example.newsassignment.data.remote.RemoteDataSource
import com.example.newsassignment.domain.DataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {

    @Binds
    @Singleton
    abstract fun provideRemoteDataSource(dataSource: RemoteDataSource): DataSource

}