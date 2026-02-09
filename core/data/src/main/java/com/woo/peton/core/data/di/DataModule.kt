package com.woo.peton.core.data.di

import com.woo.peton.core.data.impl.AuthRepositoryImpl
import com.woo.peton.core.data.impl.BannerRepositoryImpl
import com.woo.peton.core.data.impl.DetectiveRepositoryImpl
import com.woo.peton.core.data.impl.LocationRepositoryImpl
import com.woo.peton.core.data.impl.MyPetRepositoryImpl
import com.woo.peton.core.data.impl.ReportPostRepositoryImpl
import com.woo.peton.domain.repository.AuthRepository
import com.woo.peton.domain.repository.BannerRepository
import com.woo.peton.domain.repository.DetectiveRepository
import com.woo.peton.domain.repository.LocationRepository
import com.woo.peton.domain.repository.MyPetRepository
import com.woo.peton.domain.repository.ReportPostRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
@Suppress("unused")
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

    @Binds
    @Singleton
    internal abstract fun bindLocationRepository(
        impl: LocationRepositoryImpl
    ): LocationRepository
}