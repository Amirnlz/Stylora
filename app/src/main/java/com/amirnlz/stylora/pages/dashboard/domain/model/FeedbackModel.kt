package com.amirnlz.stylora.pages.dashboard.domain.model

import android.net.Uri

data class FeedbackModel(
    val imageUri: Uri,
    val feedbackType: FeedbackType,
    val language: FeedbackLanguage,
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


