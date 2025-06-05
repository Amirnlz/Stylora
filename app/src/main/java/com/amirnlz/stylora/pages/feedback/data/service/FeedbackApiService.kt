package com.amirnlz.stylora.pages.feedback.data.service

import com.amirnlz.stylora.pages.feedback.data.model.FeedbackResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface FeedbackApiService {

    @Multipart
    @POST("api/v1/feedback/give-feedback")
    suspend fun postFeedback(
        @Part imageFile: MultipartBody.Part,
        @Part("feedback_type") feedbackType: RequestBody,
        @Part("device_id") deviceId: RequestBody,
        @Part("language") language: RequestBody
    ): Response<FeedbackResponse>

    @GET("api/v1/feedback/user-feedbacks/{device_id}")
    suspend fun getFeedbackHistory(
        @Path("device_id")
        deviceId: String,
    ): Response<List<FeedbackResponse>>

}