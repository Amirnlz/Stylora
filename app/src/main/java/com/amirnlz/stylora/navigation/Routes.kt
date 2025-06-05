package com.amirnlz.stylora.navigation

import com.amirnlz.stylora.pages.dashboard.data.model.FeedbackResponse
import kotlinx.serialization.Serializable

sealed interface Routes {
    @Serializable
    object Dashboard : Routes

    @Serializable

    object History : Routes

    @Serializable
    data class Feedback(val feedbackJson: String) : Routes
}