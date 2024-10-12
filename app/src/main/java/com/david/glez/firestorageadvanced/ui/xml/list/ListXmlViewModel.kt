package com.david.glez.firestorageadvanced.ui.xml.list

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
class ListXmlViewModel @Inject constructor(private val storageService: StorageService): ViewModel() {

    private var _uiState = MutableStateFlow(ListUIState(isLoading = false, images = emptyList()))
    val uiState: StateFlow<ListUIState> = _uiState

    fun getAllImages() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            val result = withContext(Dispatchers.IO) {
                storageService.getAllImages().map { it.toString() }
            }
            _uiState.value = _uiState.value.copy(isLoading = false, images = result)
        }
    }

}

data class ListUIState(
    val isLoading: Boolean,
    val images: List<String>
)