package com.amirnlz.stylora.pages.dashboard.domain.useCase

import com.amirnlz.stylora.pages.dashboard.data.model.FeedbackResponse
import com.amirnlz.stylora.pages.dashboard.domain.model.FeedbackModel
import com.amirnlz.stylora.pages.dashboard.domain.repo.DashboardRepository
import javax.inject.Inject

class GiveFeedbackUseCase @Inject constructor(
    private val dashboardRepository: DashboardRepository
) {
    suspend operator fun invoke(
        feedbackModel: FeedbackModel
    ): Result<FeedbackResponse> {
        return dashboardRepository.giveFeedback(feedbackModel)
    }
}