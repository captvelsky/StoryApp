package com.captvelsky.storyapp.ui.activity

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.content.FileProvider
import androidx.lifecycle.viewModelScope
import com.captvelsky.storyapp.R
import com.captvelsky.storyapp.createCustomTempFile
import com.captvelsky.storyapp.databinding.ActivityUploadBinding
import com.captvelsky.storyapp.rotateBitmap
import com.captvelsky.storyapp.ui.activity.HomeActivity.Companion.EXTRA_TOKEN
import com.captvelsky.storyapp.ui.model.UploadViewModel
import com.captvelsky.storyapp.uriToFile
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream

@AndroidEntryPoint
class UploadActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUploadBinding
    private lateinit var currentPhotoPath: String
    private lateinit var token: String
    private var getFile: File? = null
    private val viewModel: UploadViewModel by viewModels()

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == RESULT_OK) {
            val myFile = File(currentPhotoPath)
            val result: Bitmap = rotateBitmap(
                BitmapFactory.decodeFile(myFile.path),
                true
            )
            val fos = FileOutputStream(myFile)
            result.compress(Bitmap.CompressFormat.JPEG, 100, fos)
            fos.flush()
            fos.close()
            getFile = myFile
            binding.ivCreateStory.setImageBitmap(result)
        }
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg: Uri = result.data?.data as Uri
            val file = uriToFile(selectedImg, this@UploadActivity)
            getFile = file
            binding.ivCreateStory.setImageURI(selectedImg)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUploadBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)


        viewModel.viewModelScope.launch {
            viewModel.getAuthToken().collect {
                token = it!!
            }
        }

        binding.apply {
            btnCamera.setOnClickListener {
                startCamera()
            }
            btnGallery.setOnClickListener {
                startGallery()
            }
            btnUpload.setOnClickListener {
                startUpload()
            }
        }
    }

    private fun startCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.resolveActivity(packageManager)

        createCustomTempFile(application).also {
            val photoURI: Uri = FileProvider.getUriForFile(
                this,
                "com.captvelsky.storyapp",
                it
            )
            currentPhotoPath = it.absolutePath
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            launcherIntentCamera.launch(intent)
        }
    }

    private fun startGallery() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, getString(R.string.choose_picture))
        launcherIntentGallery.launch(chooser)
    }

    private fun startUpload() {
        if (getFile != null) {
            showLoading(true)
            val desc = binding.etDesc
            val file = reduceFileImage(getFile as File)
            val description =
                binding.etDesc.text.toString().toRequestBody("text/plain".toMediaType())
            val requestImageFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
            val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
                "photo", file.name, requestImageFile
            )

            viewModel.viewModelScope.launch {
                viewModel.uploadImage(token, imageMultipart, description).collect { response ->
                    response.onSuccess {
                        Intent(this@UploadActivity, HomeActivity::class.java).also {
                            it.putExtra(EXTRA_TOKEN, token)
                            startActivity(it)
                            finish()
                        }
                    }
                    response.onFailure {
                        if (desc.text.toString().isEmpty()) {
                            Toast.makeText(
                                this@UploadActivity,
                                getString(R.string.story_desc_warning),
                                Toast.LENGTH_SHORT
                            ).show()
                            showLoading(false)
                        } else {
                            Toast.makeText(
                                this@UploadActivity,
                                getString(R.string.upload_failed),
                                Toast.LENGTH_SHORT
                            ).show()
                            showLoading(false)
                        }
                    }
                }
            }

        } else {
            showLoading(false)
            Toast.makeText(
                this,
                getString(R.string.upload_warning),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun reduceFileImage(file: File): File {
        val bitmap = BitmapFactory.decodeFile(file.path)
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

    private fun showLoading(state: Boolean) {
        if (state) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }
}