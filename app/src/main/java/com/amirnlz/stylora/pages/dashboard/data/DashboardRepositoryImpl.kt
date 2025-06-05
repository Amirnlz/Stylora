package com.amirnlz.stylora.pages.dashboard.data

import com.amirnlz.stylora.pages.dashboard.data.data_source.DashboardRemoteDataSource
import com.amirnlz.stylora.pages.feedback.data.model.FeedbackResponse
import com.amirnlz.stylora.pages.dashboard.domain.model.FeedbackModel
import com.amirnlz.stylora.pages.dashboard.domain.repo.DashboardRepository
import com.amirnlz.stylora.pages.feedback.data.data_source.FeedbackRemoteDataSource
import javax.inject.Inject

class DashboardRepositoryImpl @Inject constructor(
    private val remoteDataSource: FeedbackRemoteDataSource
) : DashboardRepository {
    override suspend fun giveFeedback(feedbackModel: FeedbackModel): Result<FeedbackResponse> {
        return try {
            val response = remoteDataSource.giveFeedback(
                imageUri = feedbackModel.imageUri,
                feedbackType = feedbackModel.feedbackType.name,
                language = feedbackModel.language.name,
            )
            if (response.isSuccessful) {
                Result.success(response.body()!!)
            } else {
                Result.failure(
                    Exception(
                        "Failed to submit feedback: ${
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