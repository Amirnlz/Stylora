package com.amirnlz.stylora.pages.history.domain.use_cases

import com.amirnlz.stylora.pages.history.domain.repo.HistoryRepository
import javax.inject.Inject

class GetFeedbackHistoryUseCase @Inject constructor(
    private val historyRepository: HistoryRepository
) {
    suspend operator fun invoke() = historyRepository.getFeedbackHistory()
}