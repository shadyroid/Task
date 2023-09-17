package com.xontel.task.ui.videos

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
import com.xontel.domain.entity.beans.VideoBean
import com.xontel.task.classes.adapters.VideosAdapter
import com.xontel.task.databinding.FragmentVideosBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class VideosFragment : Fragment() {
    private var _binding: FragmentVideosBinding? = null
    private val binding get() = _binding!!
    private val viewModel: VideosViewModel by viewModels()

    @Inject
    lateinit var videosAdapter: VideosAdapter
    private val requestVideosPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            requestVideos()
        } else {
            binding.root.findNavController().popBackStack()
        }
    }

    private fun getVideosPermission(): String {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Manifest.permission.READ_MEDIA_VIDEO
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
            _binding = FragmentVideosBinding.inflate(inflater, container, false)
            init()
        }
        return binding.root
    }

    private fun init() {
        binding.fabImages.setOnClickListener {
            binding.root.findNavController()
                .navigate(VideosFragmentDirections.actionNavVideosBackToNavImages())
        }
        initVideosAdapter()
        initObserves()
        requestVideosPermissionLauncher.launch(getVideosPermission())
    }


    private fun initVideosAdapter() {
        binding.rvVideos.adapter = videosAdapter
    }

    private fun initObserves() {
        lifecycleScope.launch {
            viewModel.videosResponseMutableStateFlow.collect {
                if (it != null) onVideosResponse(it)
            }
        }


    }

    private fun requestVideos() {
        viewModel.requestVideos(requireActivity())
    }


    private fun onVideosResponse(videos: List<VideoBean>) {
        videosAdapter.addData(videos)
    }


}