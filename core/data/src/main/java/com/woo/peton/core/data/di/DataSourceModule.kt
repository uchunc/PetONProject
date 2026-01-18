package com.woo.peton.core.data.di

import com.woo.peton.core.data.datasource.AuthDataSource
import com.woo.peton.core.data.remote.impl.AuthRemoteDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataSourceModule {

    @Binds
    @Singleton
    abstract fun bindAuthDataSource(
        impl: AuthRemoteDataSourceImpl
    ): AuthDataSource
}