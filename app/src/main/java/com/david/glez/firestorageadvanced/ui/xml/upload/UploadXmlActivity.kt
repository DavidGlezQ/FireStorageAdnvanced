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
import com.david.glez.firestorageadvanced.databinding.ActivityUploadXmlBinding
import com.david.glez.firestorageadvanced.databinding.DialogImageSelectorBinding
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

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
            uploadXmlViewModel.uploadBasicImage(uri)
        }
    }

    private var intentGalleryLauncher = registerForActivityResult(GetContent()) { uri ->
        uri?.let {
            uploadXmlViewModel.uploadBasicImage(it)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUploadXmlBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initUI()
    }

    private fun initUI() {
        initListeners()
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

    private fun createFile(): File {
        val name: String = SimpleDateFormat("yyyyMMdd_hhmmss").format(Date()) + "image"
        return File.createTempFile(name, ".jpg", getExternalFilesDir("my_image"))
    }
}