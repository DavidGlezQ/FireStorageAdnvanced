package com.david.glez.firestorageadvanced.ui.compose.upload

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.david.glez.firestorageadvanced.data.StorageService
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@HiltViewModel
class UploadComposeViewModel @Inject constructor(private val storageService: StorageService) :
    ViewModel() {

    fun uploadBasicImage(uri: Uri) {
        storageService.uploadBasicImage(uri)
    }

    private var _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun uploadAndGetImage(uri: Uri, onSuccessDownload: (Uri) -> (Unit)) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val result = withContext(Dispatchers.IO) {
                    storageService.uploadAndDownloadImage(uri)
                }
                onSuccessDownload(result)
            } catch (e: Exception) {
                Log.e("error", e.message.orEmpty())
            }
            _isLoading.value = false
        }
    }
}