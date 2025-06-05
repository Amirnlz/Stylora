package com.amirnlz.stylora.pages.dashboard.domain.model

import android.net.Uri

data class FeedbackModel(
    val imageUri: Uri,
    val feedbackType: String,
    val language: String,
    val deviceId: String,
    )


