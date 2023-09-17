package com.xontel.task.classes.di

import com.xontel.domain.repo.GalleryRepo
import com.xontel.domain.usecase.GalleryUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Provides
    fun provideGalleryUseCase(galleryRepo: GalleryRepo): GalleryUseCase {
        return GalleryUseCase(galleryRepo)
    }
}