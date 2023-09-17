package com.xontel.domain.repo

import android.app.Activity
import com.xontel.domain.entity.beans.ImageBean
import com.xontel.domain.entity.beans.VideoBean

interface GalleryRepo {
    suspend fun requestImages(activity: Activity): List<ImageBean>
    suspend fun requestVideos(activity: Activity): List<VideoBean>
}