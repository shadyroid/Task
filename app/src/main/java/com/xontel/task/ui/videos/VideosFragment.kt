package com.xontel.task.ui.videos

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.softxpert.petfinder.databinding.FragmentVideosBinding
import com.xontel.domain.entity.beans.VideoBean
import com.xontel.task.classes.adapters.VideosAdapter
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
        initVideosAdapter()
        initObserves()
        requestVideos()
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