package com.amirnlz.stylora.pages.dashboard.domain.useCase

import com.amirnlz.stylora.pages.dashboard.data.model.FeedbackResponse
import com.amirnlz.stylora.pages.dashboard.domain.model.FeedbackModel
import com.amirnlz.stylora.pages.dashboard.domain.repo.DashboardRepository
import com.amirnlz.stylora.pages.dashboard.ui.UserSelectionModel
import javax.inject.Inject

class GiveFeedbackUseCase @Inject constructor(
    private val dashboardRepository: DashboardRepository
) {

    suspend operator fun invoke(
        userSelectionModel: UserSelectionModel
    ): Result<FeedbackResponse> {
//        val androidId = Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)
//        val id =  Settings.Secure.getString(
//            getContext()?.contentResolver,
//            Settings.Secure.ANDROID_ID
//        )

        return dashboardRepository.giveFeedback(
            FeedbackModel(
                imageUri = userSelectionModel.imageUri!!,
                feedbackType = userSelectionModel.feedbackType.name,
                language = userSelectionModel.language.name,
                deviceId = android.os.Build.MODEL
            )
        )
    }
}