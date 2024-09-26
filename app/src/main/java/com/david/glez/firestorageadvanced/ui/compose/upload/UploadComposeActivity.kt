package com.david.glez.firestorageadvanced.ui.compose.upload

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.david.glez.firestorageadvanced.databinding.ActivityUploadComposeBinding

class UploadComposeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUploadComposeBinding

    companion object {
        fun create(context: Context): Intent = Intent(context, UploadComposeActivity::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUploadComposeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.composeView.setContent {
            UploadScreen()
        }
    }

    @Composable
    fun UploadScreen(modifier: Modifier = Modifier) {

    }
}