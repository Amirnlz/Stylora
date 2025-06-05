package com.amirnlz.stylora.pages.history.ui

import com.amirnlz.stylora.pages.feedback.data.model.FeedbackResponse

sealed interface HistoryUiState {
    object Loading : HistoryUiState
    data class Success(val items: List<FeedbackResponse>) : HistoryUiState
    data class Error(val message: String) : HistoryUiState
}