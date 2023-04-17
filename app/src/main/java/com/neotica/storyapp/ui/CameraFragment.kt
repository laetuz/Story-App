package com.neotica.storyapp.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.neotica.storyapp.databinding.FragmentCameraBinding
import com.neotica.storyapp.databinding.FragmentDetailStoryBinding
import com.neotica.storyapp.ui.viewmodel.MainViewModel
import com.neotica.storyapp.util.Constant.CAMERA_X_RESULT
import com.neotica.storyapp.util.createFile
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

class CameraFragment : Fragment() {
    private var _binding: FragmentCameraBinding? = null
    private val binding get() = _binding!!
    private val cameraSelector: CameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
    private var imageCapture: ImageCapture? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentCameraBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //insert logic here
        binding.apply {
            captureImage.setOnClickListener { takePhoto() }
            switchCamera.setOnClickListener { if (cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA) CameraSelector.DEFAULT_FRONT_CAMERA
            else CameraSelector.DEFAULT_BACK_CAMERA
                startCamera()
            }
        }
    }

    private fun takePhoto() {
       // bindingpBar.visibility = View.VISIBLE
        Toast.makeText(context, "Stand still.", Toast.LENGTH_SHORT).show()

        val imageCapture = imageCapture ?: return

        val photoFile = createFile(requireActivity().application)

        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()
        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(requireContext()),
            object : ImageCapture.OnImageSavedCallback {
                override fun onError(exc: ImageCaptureException) {
                    Toast.makeText(context, "Failed taking picture", Toast.LENGTH_SHORT).show()
                 //   binding.pBar.visibility = View.GONE
                }

                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    val intent = Intent()
                    intent.putExtra("picture", photoFile)
                    intent.putExtra(
                        "isBackCamera",
                        cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA
                    )
                    requireActivity().setResult(CAMERA_X_RESULT, intent)

                }
            }

        )

    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())

        cameraProviderFuture.addListener({
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(binding.viewFinder.surfaceProvider)
                }

            imageCapture = ImageCapture.Builder().build()

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(viewLifecycleOwner, cameraSelector,preview, imageCapture)

            } catch (exc: Exception) {
                Toast.makeText(
                    context,
                    "Failure showing the camera.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }, ContextCompat.getMainExecutor(requireContext()))
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}