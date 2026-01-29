package com.woo.peton.core.data.di

import com.woo.peton.core.data.datasource.AuthDataSource
import com.woo.peton.core.data.datasource.ImageDataSource
import com.woo.peton.core.data.datasource.MyPetDataSource
import com.woo.peton.core.data.datasource.ReportDataSource
import com.woo.peton.core.data.remote.impl.AuthRemoteDataSourceImpl
import com.woo.peton.core.data.remote.impl.ImageDataSourceImpl

import com.woo.peton.core.data.remote.impl.MyPetDataSourceImpl
import com.woo.peton.core.data.remote.impl.ReportRemoteDataSourceImpl
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

    @Binds
    @Singleton
    abstract fun bindReportDataSource(
        impl: ReportRemoteDataSourceImpl
    ): ReportDataSource

    @Binds
    @Singleton
    abstract fun bindMyPetDataSource(
        impl: MyPetDataSourceImpl
    ): MyPetDataSource

    @Binds
    @Singleton
    abstract fun bindImageDataSource(
        impl: ImageDataSourceImpl
    ): ImageDataSource
}