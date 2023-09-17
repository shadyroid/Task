package com.xontel.domain.usecase

import android.app.Activity
import com.xontel.domain.entity.beans.ImageBean
import com.xontel.domain.entity.beans.VideoBean
import com.xontel.domain.repo.GalleryRepo

class GalleryUseCase(private val galleryRepo: GalleryRepo) {
    suspend fun requestImages(activity: Activity): List<ImageBean> =
        galleryRepo.requestImages(activity)
    suspend fun requestVideos(activity: Activity): List<VideoBean> =
        galleryRepo.requestVideos(activity)
}