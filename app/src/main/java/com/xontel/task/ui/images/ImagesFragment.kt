package com.xontel.task.ui.images

import android.Manifest
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.xontel.domain.entity.beans.ImageBean
import com.xontel.task.classes.adapters.ImagesAdapter
import com.xontel.task.databinding.FragmentImagesBinding
import com.xontel.task.ui.videos.VideosFragmentDirections
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
    private val requestImagesPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            requestImages()
        } else {
            binding.root.findNavController().popBackStack()
        }
    }

    private fun getImagesPermission(): String {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Manifest.permission.READ_MEDIA_IMAGES
        } else {
            Manifest.permission.READ_EXTERNAL_STORAGE
        }
    }


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
        binding.fabVideos.setOnClickListener {
            binding.root.findNavController().navigate(
                ImagesFragmentDirections.actionNavImagesToNavPetDetails()
            )
        }
        initImagesAdapter()
        initObserves()
        requestImagesPermissionLauncher.launch(getImagesPermission())
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