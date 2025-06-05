package com.amirnlz.stylora.pages.dashboard.data.service

import com.amirnlz.stylora.pages.dashboard.data.model.FeedbackResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface DashboardApiService {

    @Multipart
    @POST("api/v1/feedback/give-feedback")
    suspend fun giveFeedback(
        @Part imageFile: MultipartBody.Part,
        @Part("feedback_type") feedbackType: RequestBody,
        @Part("device_id") deviceId: RequestBody,
        @Part("language") language: RequestBody
    ): Response<FeedbackResponse>
}