package com.neotica.storyapp.ui

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
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
import com.neotica.storyapp.util.Constant.CAMERA_X_RESULT
import com.neotica.storyapp.util.rotateBitmap
import com.neotica.storyapp.util.uriToFile
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream

class AddStoryFragment : Fragment() {
    private var _binding: FragmentAddStoryBinding? = null
    private val binding get() = _binding!!
    private var getFile: File? = null
    private  var isBackCamera:Boolean = false
    private lateinit var currentPhotoPath: String
    private val viewModel: AddStoryViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentAddStoryBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //insert logic here
        val prefLogin = LoginPreferences(requireContext())
        val token = prefLogin.getToken()
        if (token.isNullOrEmpty()){
            val action = MainFragmentDirections.actionMainFragmentToLogin()
            findNavController().navigate(action)
        }
        letsBind()
    }

    private fun letsBind(){
        binding.apply {
            btnCamera.setOnClickListener {
                startTakePhoto()
            }
            btnGallery.setOnClickListener { startGallery() }
            btnUpload.setOnClickListener {
                uploadImage()
            }
        }
    }

    private fun startTakePhoto() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.resolveActivity(requireContext().packageManager)

        createTempFile("temp_", ".jpg", requireContext().applicationContext.cacheDir).also {
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
            val resultModule = rotateBitmap(BitmapFactory.decodeFile(myFile.path))

            binding.ivPreview.setImageBitmap(resultModule)
            Log.d("neotica","camera inserted into iv")
        }
    }

    private fun uploadImage() {
        showLoading(true)
        if (getFile != null) {
            val desc = binding.etDesc.text.toString()
            if (desc.isEmpty()){
                binding.etDesc.error = "Enter Description"
                binding.etDesc.requestFocus()
            }else{
                val file = reduceFileImage(getFile as File,isBackCamera)
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
                viewModel.responseUpload.observe(viewLifecycleOwner){
                    when (it){
                        is ApiResult.Success -> {
                            Toast.makeText(context, "Photo has been uploaded.", Toast.LENGTH_SHORT).show()
                            val action = AddStoryFragmentDirections.actionAddStoryFragmentToMainFragment()
                            showLoading(false)
                            findNavController().navigate(action)
                            findNavController().popBackStack()
                        }
                        is ApiResult.Error -> {}
                        is ApiResult.Loading -> {}
                    }
                }
                viewModel.uploadStory(token,imageMultipart,description)

            }
        } else {
            Toast.makeText(context, "Please input the picture first.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun reduceFileImage(file: File,isBackCamera:Boolean): File {
        var bitmap = BitmapFactory.decodeFile(file.path)
        bitmap = rotateBitmap(bitmap, isBackCamera)
        var compressQuality = 100
        var streamLength: Int
        do {
            val bmpStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, compressQuality, bmpStream)
            val bmpPicByteArray = bmpStream.toByteArray()
            streamLength = bmpPicByteArray.size
            compressQuality -= 5
        } while (streamLength > 1000000)
        bitmap.compress(Bitmap.CompressFormat.JPEG, compressQuality, FileOutputStream(file))

        return file
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
            Log.d("neotica","Image inserted into iv")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}