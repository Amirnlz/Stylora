package com.amirnlz.stylora.pages.feedback.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FeedbackResponse(
    @SerialName("Fit Score") val fitScore: Double,
    @SerialName("Fit Feedback") val fitFeedback: String,
    @SerialName("Color Score") val colorScore: Double,
    @SerialName("Color Feedback") val colorFeedback: String,
    @SerialName("Style Score") val styleScore: Double,
    @SerialName("Style Feedback") val styleFeedback: String,
    @SerialName("Trend Alignment Score") val trendAlignmentScore: Double,
    @SerialName("Trend Feedback") val trendFeedback: String,
    @SerialName("Total Score") val totalScore: Double,
    @SerialName("Overall Feedback") val overallFeedback: String,
    @SerialName("image_url") val imageURL: String,
    @SerialName("feedback_type") val feedbackType: String,
    @SerialName("created_at") val createdAt: String
)