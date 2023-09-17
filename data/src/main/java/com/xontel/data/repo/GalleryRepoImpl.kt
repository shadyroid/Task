package com.xontel.data.repo

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentUris
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import com.xontel.domain.entity.beans.ImageBean
import com.xontel.domain.entity.beans.VideoBean
import com.xontel.domain.repo.GalleryRepo

class GalleryRepoImpl : GalleryRepo {
    @SuppressLint("Range")
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
                MediaStore.Images.Media.DATE_ADDED + " DESC"
            )
            cursor?.use {
                while (cursor.moveToNext()) {
                    val dataPath = ContentUris.withAppendedId(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        cursor.getLong(cursor.getColumnIndex(MediaStore.Images.Media._ID))
                    ).toString()
                    data.add(ImageBean(dataPath))
                }
            }
        } catch (ignored: Exception) {
        }
        return data
    }

    @SuppressLint("Range")
    override suspend fun requestVideos(activity: Activity): List<VideoBean> {
        val data: MutableList<VideoBean> = mutableListOf()
        val allVideosUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
        val projection = arrayOf(
            MediaStore.Video.VideoColumns.DATA,
            MediaStore.Video.Media._ID
        )

        try {
            val cursor: Cursor? = activity.contentResolver.query(
                allVideosUri,
                projection,
                null,
                null,
                MediaStore.Video.Media.DATE_ADDED + " DESC"
            )

            cursor?.use {
                while (cursor.moveToNext()) {
                    val videoIdColumnIndex = cursor.getColumnIndex(MediaStore.Video.Media._ID)

                    if (videoIdColumnIndex != -1 && !cursor.isNull(videoIdColumnIndex)) {
                        val thumbnailUri = Uri.withAppendedPath(
                            MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                            cursor.getLong(videoIdColumnIndex).toString()
                        )
                        data.add(VideoBean(thumbnailUri.toString()))
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return data
    }


}