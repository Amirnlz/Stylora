package com.amirnlz.stylora.pages.dashboard.ui

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amirnlz.stylora.pages.feedback.data.model.FeedbackResponse
import com.amirnlz.stylora.pages.dashboard.domain.model.FeedbackLanguage
import com.amirnlz.stylora.pages.dashboard.domain.model.FeedbackModel
import com.amirnlz.stylora.pages.dashboard.domain.model.FeedbackType
import com.amirnlz.stylora.pages.dashboard.domain.useCase.GiveFeedbackUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
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

    private val _feedbackModel = MutableStateFlow<FeedbackModel>(
        FeedbackModel(
            imageUri = Uri.EMPTY,
            feedbackType = FeedbackType.NORMAL,
            language = FeedbackLanguage.ENGLISH
        )
    )
    val feedbackModel = _feedbackModel.asStateFlow()

    fun changeImage(uri: Uri) {
        _feedbackModel.value = _feedbackModel.value.copy(imageUri = uri)
    }

    fun changeFeedbackType(feedbackType: FeedbackType) {
        _feedbackModel.value = _feedbackModel.value.copy(feedbackType = feedbackType)
    }

    fun changeLanguage(language: FeedbackLanguage) {
        _feedbackModel.value = _feedbackModel.value.copy(language = language)
    }

    fun uploadImage() {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.value = DashboardUiState.Loading
            val result = giveFeedbackUseCase.invoke(_feedbackModel.value)
            result.onSuccess { response ->
                _uiState.value = DashboardUiState.Idle
                _navigationEvents.emit(response)
            }.onFailure { exception ->
                _uiState.value = DashboardUiState.Error(exception.message ?: "Failed")
            }
        }
    }
}