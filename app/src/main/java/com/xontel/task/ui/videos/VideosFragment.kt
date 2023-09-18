package com.xontel.task.ui.videos

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.xontel.domain.entity.beans.VideoBean
import com.xontel.task.R
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
    private var isSettingsOpened: Boolean = false
    private val requestVideosPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            viewModel.requestVideos(requireActivity())
            binding.btnRequestPermission.visibility = View.GONE
        } else {
            if (!shouldShowRequestPermissionRationale(getVideosPermission())) {
                showPermissionSettingsDialog();
            } else {
                binding.btnRequestPermission.visibility = View.VISIBLE
            }
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
        requestVideosPermissionLauncher.launch(getVideosPermission())
        return binding.root
    }

    private fun init() {
        initVideosAdapter()
        initObserves()
        binding.btnRequestPermission.setOnClickListener {
            requestVideosPermissionLauncher.launch(
                getVideosPermission()
            )
        }
    }

    override fun onResume() {
        super.onResume()
        if (isSettingsOpened) {
            isSettingsOpened = false
            requestVideosPermissionLauncher.launch(getVideosPermission())
        }
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

    private fun onVideosResponse(videos: List<VideoBean>) {
        binding.llNoVideosContainer.visibility = if (videos.isEmpty()) View.VISIBLE else View.GONE
        videosAdapter.addData(videos)
    }
    private fun getVideosPermission(): String {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Manifest.permission.READ_MEDIA_VIDEO
        } else {
            Manifest.permission.READ_EXTERNAL_STORAGE
        }
    }

    private fun showPermissionSettingsDialog() {
        context?.let {
            val builder: AlertDialog.Builder = AlertDialog.Builder(it)
            builder.setTitle(getString(R.string.permission_required))
            builder.setMessage(
                getString(R.string.to_use_this_app_you_must_grant_storage_permission_please_go_to_app_settings_and_enable_the_storage_permission)
            )
            builder.setPositiveButton(getString(R.string.open_settings)) { _, _ -> openAppSettings() }
            builder.setNegativeButton(getString(R.string.cancel)) { _, _ ->
                binding.btnRequestPermission.visibility = View.VISIBLE
            }
            builder.show()
        }
    }

    private fun openAppSettings() {
        isSettingsOpened = true
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = Uri.fromParts("package", activity?.packageName, null)
        intent.data = uri
        startActivity(intent)
    }
}