package com.woo.peton.core.data.di

import com.woo.peton.core.data.repository.impl.*
import com.woo.peton.domain.repository.*
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {

    @Binds
    @Singleton
    internal abstract fun bindAuthRepository(
        impl: AuthRepositoryImpl
    ): AuthRepository

    @Binds
    @Singleton
    internal abstract fun bindPetRepository(
        impl: MyPetRepositoryImpl
    ): MyPetRepository

    @Binds
    @Singleton
    internal abstract fun bindBannerRepository(
        impl: BannerRepositoryImpl
    ): BannerRepository

    @Binds
    @Singleton
    internal abstract fun bindDetectiveRepository(
        impl: DetectiveRepositoryImpl
    ): DetectiveRepository

    @Binds
    @Singleton
    internal abstract fun bindMissingPetRepository(
        impl: ReportPostRepositoryImpl
    ): ReportPostRepository
}