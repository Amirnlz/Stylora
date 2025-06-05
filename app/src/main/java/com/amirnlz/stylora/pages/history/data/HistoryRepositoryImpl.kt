package com.amirnlz.stylora.pages.history.data

import com.amirnlz.stylora.pages.feedback.data.data_source.FeedbackRemoteDataSource
import com.amirnlz.stylora.pages.feedback.data.model.FeedbackResponse
import com.amirnlz.stylora.pages.history.domain.repo.HistoryRepository
import javax.inject.Inject

class HistoryRepositoryImpl @Inject constructor(
    private val remoteDataSource: FeedbackRemoteDataSource
) : HistoryRepository {

    override suspend fun getFeedbackHistory(): Result<List<FeedbackResponse>> {
        return try {
            val response = remoteDataSource.getFeedbackHistory()
            if (response.isSuccessful) {
                Result.success(response.body()!!)
            } else {
                Result.failure(
                    Exception(
                        "Failed to feedbacks: ${
                            response.body()?.toString()
                        }"
                    )
                )
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}