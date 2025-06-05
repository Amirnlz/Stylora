package com.amirnlz.stylora.pages.dashboard.data.data_source

import android.net.Uri
import com.amirnlz.stylora.pages.dashboard.data.model.FeedbackResponse
import retrofit2.Response

interface DashboardRemoteDataSource {

    suspend fun giveFeedback(
        imageUri: Uri,
        feedbackType: String,
        language: String,
        deviceId: String,
    ): Response<FeedbackResponse>
}