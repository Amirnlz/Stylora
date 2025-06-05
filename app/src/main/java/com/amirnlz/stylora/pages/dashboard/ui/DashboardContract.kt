package com.amirnlz.stylora.pages.dashboard.ui

import android.net.Uri
import com.amirnlz.stylora.pages.dashboard.data.model.FeedbackResponse


sealed class DashboardUiState {
    object Idle : DashboardUiState()
    object Loading : DashboardUiState()
    data class Error(val message: String) : DashboardUiState()
}

data class UserSelectionModel(
    val imageUri: Uri? = null,
    val feedbackType: FeedbackType = FeedbackType.NORMAL,
    val language: FeedbackLanguage = FeedbackLanguage.ENGLISH
)


enum class FeedbackType {
    NORMAL,
    STRICT,
}

enum class FeedbackLanguage {
    ENGLISH,
    TURKISH,
    FRENCH,
    RUSSIAN,
    ARABIC,
}
