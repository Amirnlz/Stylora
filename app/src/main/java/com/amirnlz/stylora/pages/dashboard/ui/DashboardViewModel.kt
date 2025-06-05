package com.amirnlz.stylora.pages.dashboard.ui

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amirnlz.stylora.pages.dashboard.data.model.FeedbackResponse
import com.amirnlz.stylora.pages.dashboard.domain.useCase.GiveFeedbackUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val giveFeedbackUseCase: GiveFeedbackUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow<DashboardUiState>(DashboardUiState.Idle)
    val uiState = _uiState.asStateFlow()

    private val _navigationEvents = MutableSharedFlow<FeedbackResponse>()
    val navigationEvents = _navigationEvents.asSharedFlow()

    private val _userSelection = MutableStateFlow<UserSelectionModel>(UserSelectionModel())
    val userSelection = _userSelection.asStateFlow()

    fun changeImage(uri: Uri?) {
        _userSelection.value = _userSelection.value.copy(imageUri = uri)
    }

    fun changeFeedbackType(feedbackType: FeedbackType) {
        _userSelection.value = _userSelection.value.copy(feedbackType = feedbackType)
    }

    fun changeLanguage(language: FeedbackLanguage) {
        _userSelection.value = _userSelection.value.copy(language = language)
    }

    fun uploadImage() {
        viewModelScope.launch {
            _uiState.value = DashboardUiState.Loading
            val result = giveFeedbackUseCase.invoke(_userSelection.value)
            result.onSuccess { response ->
                _uiState.value = DashboardUiState.Idle
                _navigationEvents.emit(response)
            }.onFailure { exception ->
                _uiState.value = DashboardUiState.Error(exception.message ?: "Failed")
            }
        }
    }
}