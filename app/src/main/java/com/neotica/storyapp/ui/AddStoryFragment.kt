package com.neotica.storyapp.ui

import android.animation.ObjectAnimator
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.neotica.storyapp.databinding.FragmentAddStoryBinding
import com.neotica.storyapp.models.ApiResult
import com.neotica.storyapp.models.LoginPreferences
import com.neotica.storyapp.ui.viewmodel.AddStoryViewModel
import com.neotica.storyapp.util.handleSamplingAndRotationBitmap
import com.neotica.storyapp.util.reduceFileImage
import com.neotica.storyapp.util.uriToFile
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File
import java.io.IOException

class AddStoryFragment : Fragment() {
    private var _binding: FragmentAddStoryBinding? = null
    private val binding get() = _binding!!
    private var getFile: File? = null
    private var isBackCamera: Boolean = false
    private lateinit var currentPhotoPath: String
    private val viewModel: AddStoryViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddStoryBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val prefLogin = LoginPreferences(requireContext())
        val token = prefLogin.getToken()
        if (token.isNullOrEmpty()) {
            val action = MainFragmentDirections.actionMainFragmentToLogin()
            findNavController().navigate(action)
        }
        letsBind()
        showAnimation()
    }

    private fun letsBind() {
        binding.apply {
            btnCamera.setOnClickListener {
                startTakePhoto()
            }
            btnGallery.setOnClickListener { startGallery() }
            btnUpload.setOnClickListener {
                uploadImage()
            }
            btnLocation.setOnClickListener { Toast.makeText(context, "Feature not yet available.", Toast.LENGTH_SHORT).show() }
        }
    }

    private fun startTakePhoto() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.resolveActivity(requireContext().packageManager)

        File.createTempFile("temp_", ".jpg", requireContext().applicationContext.cacheDir).also {
            val photoURI: Uri = FileProvider.getUriForFile(
                requireContext(),
                "com.neotica.storyapp",
                it
            )
            currentPhotoPath = it.absolutePath
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            launcherIntentCamera.launch(intent)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }


    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == Activity.RESULT_OK) {
            val myFile = File(currentPhotoPath)
            getFile = myFile

            try {
                val bitmap = handleSamplingAndRotationBitmap(requireContext(), Uri.fromFile(myFile))
                binding.ivPreview.setImageBitmap(bitmap)
                Log.d("neotica", "camera inserted into iv")
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    private fun uploadImage() {
        showLoading(true)
        if (getFile != null) {
            val desc = binding.etDesc.text.toString()
            if (desc.isEmpty()) {
                showLoading(false)
                binding.etDesc.error = "Enter Description"
                binding.etDesc.requestFocus()
            } else {
                val file = reduceFileImage(getFile as File)
                val description = desc.toRequestBody("text/plain".toMediaType())
                val requestImageFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
                val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
                    "photo",
                    file.name,
                    requestImageFile
                )
                val prefLogin = LoginPreferences(requireContext())
                var token = prefLogin.getToken().toString()
                token = "Bearer $token"
                viewModel.responseUpload.observe(viewLifecycleOwner) {
                    when (it) {
                        is ApiResult.Success -> {
                            Toast.makeText(context, "Photo has been uploaded.", Toast.LENGTH_SHORT)
                                .show()
                            val action =
                                AddStoryFragmentDirections.actionAddStoryFragmentToMainFragment()
                            showLoading(false)
                            findNavController().navigate(action)
                            findNavController().popBackStack()
                            val intent = Intent(requireActivity(), MainActivity::class.java)
                            requireActivity().finish()
                            startActivity(intent)
                        }

                        is ApiResult.Error -> {
                            showLoading(false)
                        }

                        is ApiResult.Loading -> {
                            showLoading(true)
                        }
                    }
                }
                viewModel.uploadStory(token, imageMultipart, description)

            }
        } else {
            showLoading(false)
            Toast.makeText(context, "Please input the picture first.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun startGallery() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Choose a Picture")
        launcherIntentGallery.launch(chooser)
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == AppCompatActivity.RESULT_OK) {
            val selectedImg: Uri = result.data?.data as Uri
            val myFile = uriToFile(selectedImg, requireContext())
            getFile = myFile
            binding.ivPreview.setImageURI(selectedImg)
            Log.d("neotica", "Image inserted into iv")
        }
    }

    private fun showAnimation() {
        ObjectAnimator.ofFloat(binding.tvInsert, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 3000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}