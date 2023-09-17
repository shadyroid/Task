package com.xontel.task.ui.images

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.softxpert.petfinder.databinding.FragmentImagesBinding
import com.xontel.domain.entity.beans.ImageBean
import com.xontel.task.classes.adapters.ImagesAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ImagesFragment : Fragment() {
    private var _binding: FragmentImagesBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ImagesViewModel by viewModels()

    @Inject
    lateinit var imagesAdapter: ImagesAdapter


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        if (_binding == null) {
            _binding = FragmentImagesBinding.inflate(inflater, container, false)
            init()
        }
        return binding.root
    }

    private fun init() {
        initImagesAdapter()
        initObserves()
        requestImages()
    }


    private fun initImagesAdapter() {
        binding.rvImages.adapter = imagesAdapter
    }

    private fun initObserves() {

        lifecycleScope.launch {
            viewModel.imagesResponseMutableStateFlow.collect {
                if (it != null) onImagesResponse(it)
            }
        }


    }

    private fun requestImages() {
        viewModel.requestImages(requireActivity())
    }


    private fun onImagesResponse(images: List<ImageBean>) {
        imagesAdapter.addData(images)
    }


}