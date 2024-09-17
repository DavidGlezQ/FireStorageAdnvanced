package com.david.glez.firestorageadvanced.ui.xml.upload

import androidx.lifecycle.ViewModel
import com.david.glez.firestorageadvanced.data.StorageService
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class UploadXmlViewModel @Inject constructor(private val storageService: StorageService) :
    ViewModel() {
}