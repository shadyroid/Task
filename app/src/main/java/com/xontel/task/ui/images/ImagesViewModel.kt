package com.xontel.task.ui.images

import android.app.Activity
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xontel.domain.entity.beans.ImageBean
import com.xontel.domain.usecase.GalleryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ImagesViewModel @Inject constructor(
    private val galleryUseCase: GalleryUseCase,
) : ViewModel() {

    private val _imagesResponseMutableStateFlow = MutableStateFlow<List<ImageBean>?>(null)

    val imagesResponseMutableStateFlow: StateFlow<List<ImageBean>?> =
        _imagesResponseMutableStateFlow


    fun requestImages(activity: Activity) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val images = galleryUseCase.requestImages(activity)
                _imagesResponseMutableStateFlow.value = images
            } catch (e: Exception) {
                onError(e)
            }
        }
    }


    private fun onError(e: Throwable) {
        Log.d("error ", "error : $e")
    }


}