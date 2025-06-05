package com.amirnlz.stylora.pages.dashboard.domain.repo

import com.amirnlz.stylora.pages.dashboard.data.model.FeedbackResponse
import com.amirnlz.stylora.pages.dashboard.domain.model.FeedbackModel

interface DashboardRepository {
    suspend fun giveFeedback(
        feedbackModel: FeedbackModel
    ): Result<FeedbackResponse>
}