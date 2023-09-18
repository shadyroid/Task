package com.xontel.task.ui.images

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.xontel.domain.entity.beans.ImageBean
import com.xontel.task.R
import com.xontel.task.classes.adapters.ImagesAdapter
import com.xontel.task.databinding.FragmentImagesBinding
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
    private var isSettingsOpened: Boolean = false
    private val requestImagesPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            viewModel.requestImages(requireActivity())
            binding.btnRequestPermission.visibility = GONE
        } else {
            if (!shouldShowRequestPermissionRationale(getImagesPermission())) {
                showPermissionSettingsDialog();
            } else {
                binding.btnRequestPermission.visibility = VISIBLE
            }
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
        requestImagesPermissionLauncher.launch(getImagesPermission())
        return binding.root
    }

    private fun init() {
        initImagesAdapter()
        initObserves()
        binding.btnRequestPermission.setOnClickListener {
            requestImagesPermissionLauncher.launch(
                getImagesPermission()
            )
        }
    }

    override fun onResume() {
        super.onResume()
        if (isSettingsOpened) {
            isSettingsOpened = false
            requestImagesPermissionLauncher.launch(getImagesPermission())
        }
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


    private fun onImagesResponse(images: List<ImageBean>) {
        binding.llNoImagesContainer.visibility = if (images.isEmpty()) VISIBLE else GONE
        imagesAdapter.addData(images)
    }

    private fun getImagesPermission(): String {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Manifest.permission.READ_MEDIA_IMAGES
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