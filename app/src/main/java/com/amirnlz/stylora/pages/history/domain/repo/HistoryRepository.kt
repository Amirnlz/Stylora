package com.amirnlz.stylora.pages.history.domain.repo

import com.amirnlz.stylora.pages.feedback.data.model.FeedbackResponse

interface HistoryRepository {
    suspend fun getFeedbackHistory(): Result<List<FeedbackResponse>>
}