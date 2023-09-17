package com.xontel.data.repo

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentUris
import android.database.Cursor
import android.provider.MediaStore
import com.xontel.domain.entity.beans.ImageBean
import com.xontel.domain.entity.beans.VideoBean
import com.xontel.domain.repo.GalleryRepo

class GalleryRepoImpl : GalleryRepo {
    override suspend fun requestImages(activity: Activity): List<ImageBean> {
        val data: MutableList<ImageBean> = mutableListOf()
        val allImagesUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val projection = arrayOf(MediaStore.Images.ImageColumns.DATA, MediaStore.Images.Media._ID)

        try {
            val cursor: Cursor? = activity.contentResolver.query(
                allImagesUri,
                projection,
                null,
                null,
                MediaStore.Images.Media.DATE_ADDED
            )
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    @SuppressLint("Range") val dataPath = ContentUris.withAppendedId(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        cursor.getLong(cursor.getColumnIndex(MediaStore.Images.Media._ID))
                    ).toString()
                    data.add(0, ImageBean(dataPath))
                } while (cursor.moveToNext())

                cursor.close()
            }
        } catch (ignored: Exception) {
        }
        return data
    }

    override suspend fun requestVideos(activity: Activity): List<VideoBean> {

    }

}