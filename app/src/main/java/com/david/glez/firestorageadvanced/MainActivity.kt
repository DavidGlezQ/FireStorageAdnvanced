package com.david.glez.firestorageadvanced

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.david.glez.firestorageadvanced.databinding.ActivityMainBinding
import com.david.glez.firestorageadvanced.ui.xml.upload.UploadXmlActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnXml.setOnClickListener {startActivity(UploadXmlActivity.create(this))}
        binding.btnCompose.setOnClickListener {}
    }
}