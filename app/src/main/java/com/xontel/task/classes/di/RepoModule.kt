package com.xontel.task.classes.di

import com.xontel.data.repo.GalleryRepoImpl
import com.xontel.domain.repo.GalleryRepo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object RepoModule {

    @Provides
    fun provideGalleryRepo(): GalleryRepo {
        return GalleryRepoImpl()
    }


}