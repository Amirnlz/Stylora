package com.amirnlz.stylora.pages.feedback.data.data_source

import android.net.Uri
import com.amirnlz.stylora.pages.feedback.data.model.FeedbackResponse
import retrofit2.Response

interface FeedbackRemoteDataSource {
    suspend fun giveFeedback(
        imageUri: Uri,
        feedbackType: String,
        language: String,
    ): Response<FeedbackResponse>

    suspend fun getFeedbackHistory(): Response<List<FeedbackResponse>>

}