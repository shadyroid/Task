package com.xontel.task.ui.videos

import android.app.Activity
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xontel.domain.entity.beans.VideoBean
import com.xontel.domain.usecase.GalleryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VideosViewModel @Inject constructor(
    private val galleryUseCase: GalleryUseCase,
) : ViewModel() {

    private val _videosResponseMutableStateFlow = MutableStateFlow<List<VideoBean>?>(null)

    val videosResponseMutableStateFlow: StateFlow<List<VideoBean>?> =
        _videosResponseMutableStateFlow


    fun requestVideos(activity: Activity) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val videos = galleryUseCase.requestVideos(activity)
                _videosResponseMutableStateFlow.value = videos
            } catch (e: Exception) {
                onError(e)
            }
        }
    }


    private fun onError(e: Throwable) {
        Log.d("error ", "error : $e")
    }


}