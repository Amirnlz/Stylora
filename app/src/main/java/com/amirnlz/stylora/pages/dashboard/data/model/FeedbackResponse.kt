package com.amirnlz.stylora.pages.dashboard.data.model

import kotlinx.serialization.Serializable

@Serializable
data class FeedbackResponse(
    val fitScore: Long,
    val fitFeedback: String,
    val colorScore: Long,
    val colorFeedback: String,
    val styleScore: Long,
    val styleFeedback: String,
    val trendAlignmentScore: Long,
    val trendFeedback: String,
    val totalScore: Long,
    val overallFeedback: String,
    val imageURL: String,
    val feedbackType: String,
    val createdAt: String
)
