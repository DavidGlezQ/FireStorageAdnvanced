package com.david.glez.firestorageadvanced.ui.xml.upload

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts.GetContent
import androidx.activity.result.contract.ActivityResultContracts.TakePicture
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.bumptech.glide.Glide
import com.david.glez.firestorageadvanced.databinding.ActivityUploadXmlBinding
import com.david.glez.firestorageadvanced.databinding.DialogImageSelectorBinding
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import kotlinx.coroutines.launch

@AndroidEntryPoint
class UploadXmlActivity : AppCompatActivity() {
    companion object {
        fun create(context: Context): Intent = Intent(context, UploadXmlActivity::class.java)
    }

    private lateinit var binding: ActivityUploadXmlBinding
    private val uploadXmlViewModel: UploadXmlViewModel by viewModels()
    private lateinit var uri: Uri
    private var intentCameraLauncher = registerForActivityResult(TakePicture()) {
        if (it && uri.path?.isNotEmpty() == true) {
            uploadXmlViewModel.uploadAndGetImage(uri) { downloadUri ->
                clearText()
                showNewImage(downloadUri)
            }
        }
    }
    private var intentGalleryLauncher = registerForActivityResult(GetContent()) { uri ->
        uri?.let {
            uploadXmlViewModel.uploadAndGetImage(it) { downloadUri ->
                showNewImage(downloadUri)
            }
        }
    }

    private fun showNewImage(downloadUri: Uri) {
        Glide.with(this).load(downloadUri).into(binding.ivImage)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUploadXmlBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //InitUI Methods
        initUI()
    }

    private fun initUI() {
        initListeners()
        initUIState()
    }

    private fun initUIState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                uploadXmlViewModel.isLoading.collect {
                    binding.pbImage.isVisible = it
                    if (it) {
                        binding.ivPlaceHolder.isVisible = false
                        binding.ivImage.setImageDrawable(null)
                    }
                }
            }
        }
    }

    private fun initListeners() {
        binding.fabImage.setOnClickListener {
            showImageDialog()
        }
    }

    private fun showImageDialog() {
        val dialogBinding = DialogImageSelectorBinding.inflate(layoutInflater)
        val dialog = AlertDialog.Builder(this).apply {
            setView(dialogBinding.root)
        }.create()

        dialogBinding.btnTakePhoto.setOnClickListener {
            takePhoto()
            dialog.dismiss()
        }

        dialogBinding.btnGallery.setOnClickListener {
            getImageFromGallery()
            dialog.dismiss()
        }

        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        dialog.show()
    }

    private fun getImageFromGallery() {
        intentGalleryLauncher.launch("image/*")
    }

    private fun takePhoto() {
        generateUri()
        intentCameraLauncher.launch(uri)
    }

    private fun generateUri() {
        uri = FileProvider.getUriForFile(
            Objects.requireNonNull(this),
            "com.david.glez.firestorageadvanced.provider",
            createFile()
        )
    }

    private fun clearText() {
        binding.etTitle.text?.clear()
        binding.etTitle.clearFocus()
    }

    private fun createFile(): File {
        val userTitle = binding.etTitle.text.toString().trim()
        val name: String = userTitle.ifEmpty {
            SimpleDateFormat("yyyyMMdd_hhmmss").format(Date()) + "image"
        }
        return File.createTempFile(name, ".jpg", getExternalFilesDir("my_image"))
    }
}